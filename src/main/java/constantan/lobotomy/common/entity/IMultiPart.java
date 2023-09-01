package constantan.lobotomy.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public interface IMultiPart<T extends Entity> {

    private T self() {
        return (T) this;
    }

    default void tickPart(CommonPartEntity<?> part, double offsetX, double offsetY, double offsetZ) {
        part.setPos(self().getX() + offsetX, self().getY() + offsetY, self().getZ() + offsetZ);
    }

    default void multiPartInit() {
        self().setId(CommonPartEntity.getEntityCounter().getAndAdd(this.getSubParts().length + 1) + 1);
    }

    default void multiPartTick() {
        var vec3 = new Vec3[this.getSubParts().length];
        for(int j = 0; j < this.getSubParts().length; ++j) {
            vec3[j] = new Vec3(this.getSubParts()[j].getX(), this.getSubParts()[j].getY(), this.getSubParts()[j].getZ());
        }

        this.partMovingConsumer().accept(self());

        for(int l = 0; l < this.getSubParts().length; ++l) {
            this.getSubParts()[l].xo = vec3[l].x;
            this.getSubParts()[l].yo = vec3[l].y;
            this.getSubParts()[l].zo = vec3[l].z;
            this.getSubParts()[l].xOld = vec3[l].x;
            this.getSubParts()[l].yOld = vec3[l].y;
            this.getSubParts()[l].zOld = vec3[l].z;
        }
    }

    default void multiPartPushEntities() {
        boolean flag = false;
        for (PartEntity<?> part : this.getSubParts()) {
            List<Entity> list = part.level.getEntities(part, part.getBoundingBox(), EntitySelector.pushableBy(part));
            if (!list.isEmpty()) {
                int i = part.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
                if (i > 0 && list.size() > i - 1 && new Random().nextInt(4) == 0) {
                    int j = 0;

                    for (Entity entity : list) {
                        if (!entity.isPassenger()) {
                            ++j;
                        }
                    }

                    if (j > i - 1 && !flag) {
                        flag = part.hurt(DamageSource.CRAMMING, 6.0F);
                    }
                }

                for (Entity entity : list) {
                    entity.push(part);
                }
            }
        }
    }

    PartEntity<?>[] getSubParts();

    Consumer<T> partMovingConsumer();
}

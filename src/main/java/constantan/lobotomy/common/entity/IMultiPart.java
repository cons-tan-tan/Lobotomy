package constantan.lobotomy.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public interface IMultiPart<T extends Entity, U extends PartEntity<T>> {

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    default void tickPart(CommonPartEntity<?> part, double offsetX, double offsetY, double offsetZ) {
        part.setPos(self().getX() + offsetX, self().getY() + offsetY, self().getZ() + offsetZ);
    }

    default void multiPartInit() {
        self().setId(CommonPartEntity.getEntityCounter().getAndAdd(this.getPartList().size() + 1) + 1);
    }

    default void multiPartTick() {
        List<Vec3> vecList = new ArrayList<>();
        for (PartEntity<?> part : this.getPartList()) {
            vecList.add(new Vec3(part.getX(), part.getY(), part.getZ()));
        }

        this.partMovingConsumer().accept(self());

        for (int l = 0; l < this.getPartList().size(); ++l) {
            this.getPartList().get(l).xo = vecList.get(l).x;
            this.getPartList().get(l).yo = vecList.get(l).y;
            this.getPartList().get(l).zo = vecList.get(l).z;
            this.getPartList().get(l).xOld = vecList.get(l).x;
            this.getPartList().get(l).yOld = vecList.get(l).y;
            this.getPartList().get(l).zOld = vecList.get(l).z;
        }
    }

    default void multiPartPushEntities() {
        boolean flag = false;
        for (PartEntity<?> part : this.getPartList()) {
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

    List<U> getPartList();

    Consumer<T> partMovingConsumer();
}

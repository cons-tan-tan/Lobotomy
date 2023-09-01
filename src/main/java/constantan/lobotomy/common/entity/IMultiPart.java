package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.entity.PartEntity;

public interface IMultiPart {

    private Entity self() {
        return (Entity) this;
    }

    default void tickPart(CommonPartEntity<?> part, double offsetX, double offsetY, double offsetZ) {
        part.setPos(self().getX() + offsetX, self().getY() + offsetY, self().getZ() + offsetZ);
    }

    default void multiPartInit() {
        self().setId(CommonPartEntity.getEntityCounter().getAndAdd(this.getSubParts().length + 1) + 1);
    }

    PartEntity<?>[] getSubParts();
}

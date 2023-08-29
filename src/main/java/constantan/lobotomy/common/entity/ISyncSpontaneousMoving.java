package constantan.lobotomy.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Mob;

public interface ISyncSpontaneousMoving {

    EntityDataAccessor<Boolean> IS_MOVING_SPONTANEOUSLY = SynchedEntityData
            .defineId(AbnormalityEntity.class, EntityDataSerializers.BOOLEAN);

    private Mob self() {
        return (Mob) this;
    }

    private SynchedEntityData getEntityData() {
        return self().getEntityData();
    }

    default boolean isMovingSpontaneously() {
        return this.getEntityData().get(IS_MOVING_SPONTANEOUSLY);
    }

    default void setMovingSpontaneously(boolean isMovingSpontaneously) {
        if (!self().level.isClientSide) {
            this.getEntityData().set(IS_MOVING_SPONTANEOUSLY, isMovingSpontaneously);
        }
    }
}

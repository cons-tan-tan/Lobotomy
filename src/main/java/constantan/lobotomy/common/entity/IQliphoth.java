package constantan.lobotomy.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;

public interface IQliphoth {

    EntityDataAccessor<Integer> QLIPHOTH_COUNTER = SynchedEntityData
            .defineId(AbnormalityEntity.class, EntityDataSerializers.INT);

    private AbnormalityEntity<?> self() {
        return (AbnormalityEntity<?>) this;
    }

    private SynchedEntityData getEntityData() {
        return self().getEntityData();
    }


    default int getMaxQliphothCounter() {
        return self().getAbnormalityType().getQliphothCounter();
    }

    default int getQliphothCounter() {
        return this.getEntityData().get(QLIPHOTH_COUNTER);
    }

    default void setQliphothCounter(int counterValue) {
        if (!self().level.isClientSide) {
            this.getEntityData().set(QLIPHOTH_COUNTER, Mth.clamp(counterValue, 0, this.getMaxQliphothCounter()));
        }
    }

    default void resetQliphothCounter() {
        this.setQliphothCounter(this.getMaxQliphothCounter());
    }

    default void addQliphothCounter(int add) {
        if (add > 0) {
            this.setQliphothCounter(this.getQliphothCounter() + add);
        }
    }

    default void subQliphothCounter(int sub) {
        if (sub > 0) {
            this.setQliphothCounter(this.getQliphothCounter() - sub);
        }
    }
}

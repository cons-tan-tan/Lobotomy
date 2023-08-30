package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public interface ITransientNoCulling<R extends Entity> {

    private R self() {
        return (R) this;
    }

    default void noCullingTick() {
        if (self().level.isClientSide && this.isNoCullingIf() != null) {
            self().noCulling = this.isNoCullingIf().test(self());
        }
    }

    default Predicate<R> isNoCullingIf() {
        return entity -> {
            if (entity instanceof AbnormalityEntity<?> abnormality) {
                return abnormality.getAttackTick() > 0;
            }
            return entity.noCulling;
        };
    }
}

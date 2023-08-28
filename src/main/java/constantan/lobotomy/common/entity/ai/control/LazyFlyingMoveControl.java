package constantan.lobotomy.common.entity.ai.control;

import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public class LazyFlyingMoveControl<P extends Mob> extends FlyingMoveControl implements ILazyController<P> {

    protected final P owner;

    protected Lazy<Predicate<P>> predicateLazy = Lazy.of(() -> (mob -> false));
    protected ToFloatFunction<P> deltaMovementMultiplier = mob -> 0.9F;

    public LazyFlyingMoveControl(P pMob, int pMaxTurn, boolean pHoversInPlace) {
        super(pMob, pMaxTurn, pHoversInPlace);
        this.owner = pMob;
    }

    @Override
    public LazyFlyingMoveControl<P> isLazyIf(Lazy<Predicate<P>> isLazyPredicateLazy) {
        this.predicateLazy = isLazyPredicateLazy;
        return this;
    }

    public LazyFlyingMoveControl<P> decelerateMultiplier(ToFloatFunction<P> deltaMovementMultiplierFunction) {
        this.deltaMovementMultiplier = deltaMovementMultiplierFunction;
        return this;
    }

    @Override
    public void tick() {
        if (!predicateLazy.get().test(owner)) {
            super.tick();
        } else {
            this.mob.setNoGravity(true);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(this.deltaMovementMultiplier.apply(owner)));
        }
    }
}

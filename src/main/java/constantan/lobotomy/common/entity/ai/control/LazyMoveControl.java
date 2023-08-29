package constantan.lobotomy.common.entity.ai.control;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public class LazyMoveControl<P extends Mob> extends MoveControl implements ILazyController<P> {

    protected final P owner;

    protected Lazy<Predicate<P>> predicateLazy = Lazy.of(() -> (mob -> false));

    public LazyMoveControl(P pMob) {
        super(pMob);
        this.owner = pMob;
    }

    @Override
    public LazyMoveControl<P> isLazyIf(Lazy<Predicate<P>> isLazyPredicateLazy) {
        this.predicateLazy = isLazyPredicateLazy;
        return this;
    }

    @Override
    public void tick() {
        if (!this.predicateLazy.get().test(this.owner)) {
            super.tick();
        } else {
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }
}

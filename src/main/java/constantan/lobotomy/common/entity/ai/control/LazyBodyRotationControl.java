package constantan.lobotomy.common.entity.ai.control;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public class LazyBodyRotationControl<P extends Mob> extends BodyRotationControl implements ILazyController<P> {

    protected final P owner;

    protected Lazy<Predicate<P>> predicateLazy = Lazy.of(() -> mob -> false);

    public LazyBodyRotationControl(P pMob) {
        super(pMob);
        this.owner = pMob;
    }

    @Override
    public LazyBodyRotationControl<P> isLazyIf(Lazy<Predicate<P>> isLazyPredicateLazy) {
        this.predicateLazy = isLazyPredicateLazy;
        return this;
    }

    @Override
    public void clientTick() {
        if (!this.predicateLazy.get().test(this.owner)) {
            super.clientTick();
        } else {
            this.owner.yBodyRot = this.owner.yHeadRot;
        }
    }
}

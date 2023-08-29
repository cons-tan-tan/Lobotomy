package constantan.lobotomy.common.entity.ai.control;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public class LazyLookControl<P extends Mob> extends LookControl implements ILazyController<P> {

    protected final P owner;

    protected Lazy<Predicate<P>> predicateLazy = Lazy.of(() -> mob -> false);

    public LazyLookControl(P pMob) {
        super(pMob);
        this.owner = pMob;
    }

    @Override
    public LazyLookControl<P> isLazyIf(Lazy<Predicate<P>> isLazyPredicateLazy) {
        this.predicateLazy = isLazyPredicateLazy;
        return this;
    }

    @Override
    public void tick() {
        if (!this.predicateLazy.get().test(this.owner)) {
            super.tick();
        }
    }
}

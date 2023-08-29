package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.entity.ai.control.LazyBodyRotationControl;
import constantan.lobotomy.common.entity.ai.control.LazyLookControl;
import constantan.lobotomy.common.entity.ai.control.LazyMoveControl;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface ILazyControl<P extends Mob> {

    private P self() {
        return (P) this;
    }

    default LookControl createLazyLookControl() {
        return new LazyLookControl<>(self())
                .isLazyIf(this.getPredicateLazy());
    }

    default MoveControl createLazyMoveControl() {
        return new LazyMoveControl<>(self())
                .isLazyIf(this.getPredicateLazy());
    }

    @NotNull
    default BodyRotationControl createLazyBodyControl() {
        return new LazyBodyRotationControl<>(self())
                .isLazyIf(this.getPredicateLazy());
    }

    Lazy<Predicate<P>> getPredicateLazy();
}

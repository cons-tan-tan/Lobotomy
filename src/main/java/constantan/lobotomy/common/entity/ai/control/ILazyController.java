package constantan.lobotomy.common.entity.ai.control;

import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public interface ILazyController<P> {

    ILazyController<P> isLazyIf(Lazy<Predicate<P>> isLazyPredicateLazy);
}

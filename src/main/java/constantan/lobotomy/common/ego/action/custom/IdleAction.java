package constantan.lobotomy.common.ego.action.custom;

import constantan.lobotomy.common.ego.action.IEgoAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class IdleAction<T> implements IEgoAction<T> {

    @Override
    public Function<Player, Function<ItemStack, Function<T, Boolean>>> getAction() {
        return player -> stack -> t -> true;
    }
}

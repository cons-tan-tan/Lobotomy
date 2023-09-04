package constantan.lobotomy.common.ego.action;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface IEgoAction<T> {

    Function<Player, Function<ItemStack, Function<T, Boolean>>> getAction();
}

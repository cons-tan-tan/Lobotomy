package constantan.lobotomy.common.ego.action.custom;

import constantan.lobotomy.common.ego.action.IEgoAction;
import constantan.lobotomy.common.item.EgoWeapon;
import constantan.lobotomy.common.ego.EgoUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ExtraAttackAction<T extends EgoWeapon> implements IEgoAction<T> {

    private final Function<Player, Function<ItemStack, Function<T, DamageSource>>> damageSourceFunction;
    private final Function<Player, Function<ItemStack, Function<T, Float>>> damageAmountFunction;

    public ExtraAttackAction(Function<Player, Function<ItemStack, Function<T, DamageSource>>> damageSourceFunction,
                             Function<Player, Function<ItemStack, Function<T, Float>>> damageAmountFunction) {
        this.damageSourceFunction = damageSourceFunction;
        this.damageAmountFunction = damageAmountFunction;
    }

    @Override
    public Function<Player, Function<ItemStack, Function<T, Boolean>>> getAction() {
        return player -> stack -> egoWeapon -> EgoUtil.doConsumerToTargetInRange(player, (float) player.getAttackRange(), target ->
                target.hurt(damageSourceFunction.apply(player).apply(stack).apply(egoWeapon),
                        damageAmountFunction.apply(player).apply(stack).apply(egoWeapon))
        );
    }
}

package constantan.lobotomy.common.ego.action;

import constantan.lobotomy.common.item.EgoWeapon;
import constantan.lobotomy.common.util.EgoUtil;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ExtraDamageAction<T extends EgoWeapon> implements IEgoAction<T>{

    private final Function<Player, Function<ItemStack, Function<T, IMixinDamageSource>>> damageSourceFunction;
    private final Function<Player, Function<ItemStack, Function<T, Float>>> damageAmountFunction;

    public ExtraDamageAction(Function<Player, Function<ItemStack, Function<T, IMixinDamageSource>>> iMixinDamageSourceFunction,
                             Function<Player, Function<ItemStack, Function<T, Float>>> damageAmountFunction) {
        this.damageSourceFunction = iMixinDamageSourceFunction;
        this.damageAmountFunction = damageAmountFunction;
    }

    @Override
    public Function<Player, Function<ItemStack, Function<T, Boolean>>> getAction() {
        return player -> stack -> egoWeapon -> EgoUtil.doConsumerToTargetInRange(player, (float) player.getAttackRange(), target ->
                target.hurt((DamageSource) damageSourceFunction.apply(player).apply(stack).apply(egoWeapon),
                        damageAmountFunction.apply(player).apply(stack).apply(egoWeapon)));
    }
}

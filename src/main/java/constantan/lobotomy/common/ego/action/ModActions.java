package constantan.lobotomy.common.ego.action;

import constantan.lobotomy.common.ego.action.custom.ExtraAttackAction;
import constantan.lobotomy.common.item.ego.SimpleEgoMeleeWeapon;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.world.damagesource.DamageSource;

import java.util.Set;

public class ModActions {

    public static final EgoActionBuilderManager JUSTITIA_ATTACK_NORMAL = new EgoActionBuilderManager()
            .add(new EgoActionSequencer.Builder<SimpleEgoMeleeWeapon>()
                    .action(Set.of(1, 2, 3), new ExtraAttackAction<>(
                            player -> stack -> simpleEgoMeleeWeapon ->
                                    (DamageSource) ((IMixinDamageSource) DamageSource.playerAttack(player)).ignoreInvulnerable(),
                            player -> stack -> simpleEgoMeleeWeapon ->
                                    simpleEgoMeleeWeapon.getRangedRandomDamage(stack))), 9)
            .add(new EgoActionSequencer.Builder<SimpleEgoMeleeWeapon>()
                    .action(Set.of(1, 2, 3, 4, 5, 11, 15), new ExtraAttackAction<>(
                            player -> stack -> simpleEgoMeleeWeapon ->
                                    (DamageSource) ((IMixinDamageSource) DamageSource.playerAttack(player)).ignoreInvulnerable(),
                            player -> stack -> simpleEgoMeleeWeapon ->
                                    simpleEgoMeleeWeapon.getRangedRandomDamage(stack))), 1);
}

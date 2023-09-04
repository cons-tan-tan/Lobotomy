package constantan.lobotomy.common.ego.action;

import constantan.lobotomy.common.item.EgoWeapon;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        return player -> stack -> egoWeapon -> {
            double range = player.getAttackRange();
            Vec3 eyePos = player.getEyePosition();
            Vec3 rangeVec = player.getViewVector(1.0F).normalize().scale(range);
            AABB searchArea = player.getBoundingBox().expandTowards(rangeVec).inflate(1.0F);
            EntityHitResult result = ProjectileUtil
                    .getEntityHitResult(player, eyePos, eyePos.add(rangeVec), searchArea,
                            target -> !target.isSpectator() && target.isPickable(), Math.pow(range, 2));
            if (result != null) {
                Entity target = result.getEntity();
                Vec3 hitLocation = result.getLocation();
                boolean flag = player.level
                        .clip(new ClipContext(eyePos, hitLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                        .getType() == HitResult.Type.MISS;
                if (flag) {
                    target.hurt((DamageSource) damageSourceFunction.apply(player).apply(stack).apply(egoWeapon),
                            damageAmountFunction.apply(player).apply(stack).apply(egoWeapon));
                }
            }
            return true;
        };
    }
}

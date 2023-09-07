package constantan.lobotomy.common.item;

import constantan.lobotomy.common.ego.EgoUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class EgoRangeWeapon extends EgoWeapon {

    protected static final int FIRE_ANIM_STATE = 0;

    public EgoRangeWeapon(int minDamage, int maxDamage, Properties pProperties) {
        super(minDamage, maxDamage, pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.getCooldowns().addCooldown(this, 10);
        if (!pPlayer.isOnGround() && !pPlayer.getAbilities().instabuild) {
            pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(pPlayer.getViewVector(1.0F).normalize().scale(-0.1F)));
        }
        if (!pLevel.isClientSide) {
            this.playAnimation(pPlayer, pUsedHand, FIRE_ANIM_STATE);
            EgoUtil.doConsumerToTargetInRange(pPlayer, 16, target ->
                    target.hurt(DamageSource.playerAttack(pPlayer), this.getRangedRandomDamage(stack)));
        }
        return InteractionResultHolder.fail(stack);
    }

    public static class EgoRangeWeaponProperties extends EgoWeaponProperties<EgoRangeWeaponProperties> {
    }
}

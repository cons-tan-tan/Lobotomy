package constantan.lobotomy.mixin;

import constantan.lobotomy.common.capability.sanity.PlayerSanityProvider;
import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.item.EgoMeleeWeapon;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
    private void isDamageSourceBlocked_Head(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir) {
        IMixinDamageSource damageSource = (IMixinDamageSource) (Object) pDamageSource;
        if (!damageSource.isBlockable()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            shift = At.Shift.BEFORE), ordinal = 0, argsOnly = true)
    private float hurt_Before_isDamageSourceBlocked(float pAmount, DamageSource pSource) {
        LivingEntity self = (LivingEntity) (Object) this;
        boolean isShieldAvailable = self.isDamageSourceBlocked(pSource);
        IMixinDamageSource damageSource = (IMixinDamageSource) (Object) pSource;
        if (damageSource.hasRiskLevel()) {
            RiskLevelUtil attackerRiskLevel = damageSource.getRiskLevel();
            RiskLevelUtil defenderRiskLevel;
            Map<DamageTypeUtil, Float> defenseMap;

            if (isShieldAvailable) {
                //↓仮
                defenderRiskLevel = RiskLevelUtil.ZAYIN;//盾のRiskLevel
                //↓仮
                defenseMap = DefenseUtil.DEFAULT_DEFENSE;//盾のDefense
            } else {
                defenderRiskLevel = RiskLevelUtil.getRiskLevel(self);
                defenseMap = DefenseUtil.getDefense(self);
            }

            float riskLevelRatio = RiskLevelUtil.getDamageRatio(defenderRiskLevel, attackerRiskLevel);
            DamageTypeUtil damageType = damageSource.hasDamageType()
                    ? damageSource.getDamageType()
                    //↓仮
                    : DamageTypeUtil.RED;//基本Redだけど一部の攻撃は変えるかも
            float defenseRatio = defenseMap.get(damageType);

            float ratio = riskLevelRatio * defenseRatio;
            float calculatedDamageAmount = (!(self instanceof AbnormalityEntity) && damageType == DamageTypeUtil.PALE
                    ? Math.abs(pAmount * ratio * 0.01F * self.getMaxHealth())
                    : Math.abs(pAmount * ratio));

            if (ratio < 0) {//ダメージ吸収
                if (isShieldAvailable) {
                    //盾で吸収したときの処理
                    //耐久値回復とか？
                } else {
                    if (self instanceof Player player) {
                        if (damageType == DamageTypeUtil.WHITE || damageType == DamageTypeUtil.BLACK) {//sanity回復
                            player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                                sanity.addSanityWithSync((int) calculatedDamageAmount, (ServerPlayer) player);
                            });
                        }
                        if (damageType != DamageTypeUtil.WHITE) {//HP回復
                            player.heal(calculatedDamageAmount);
                        }
                    } else {
                        self.heal(calculatedDamageAmount);
                    }
                }
                return 0.0F;
            }

            if (damageType.canAffectSanity() && self instanceof Player player) {
                player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.addSanityWithSync((int) -calculatedDamageAmount, (ServerPlayer) player);
                });
                if (damageType == DamageTypeUtil.WHITE) {
                    return 0.0F;
                }
            }

            return calculatedDamageAmount;
        }
        return pAmount;
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    private void getDamageAfterArmorAbsorb_Head(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        IMixinDamageSource damageSource = (IMixinDamageSource) (Object) pDamageSource;
        if (damageSource.hasRiskLevel()) {
            cir.setReturnValue(pDamageAmount);
        }
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("HEAD"), cancellable = true)
    private void getDamageAfterMagicAbsorb_Head(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        IMixinDamageSource damageSource = (IMixinDamageSource) (Object) pDamageSource;
        if (damageSource.hasRiskLevel()) {
            cir.setReturnValue(pDamageAmount);
        }
    }

    @Inject(method = "getAttributeValue", at = @At("HEAD"), cancellable = true)
    private void getAttributeValue_Head(Attribute pAttribute, CallbackInfoReturnable<Double> cir) {
        if (pAttribute == Attributes.ATTACK_DAMAGE) {
            LivingEntity self = (LivingEntity) (Object) this;
            ItemStack itemStack = self.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.getItem() instanceof EgoMeleeWeapon egoMeleeWeapon) {
                cir.setReturnValue(self.getAttributes().getValue(pAttribute) + egoMeleeWeapon.getRangedRandomDamage(itemStack));
            }
        }
    }
}

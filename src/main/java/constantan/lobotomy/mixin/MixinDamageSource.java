package constantan.lobotomy.mixin;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.item.EgoMeleeWeapon;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(DamageSource.class)
public abstract class MixinDamageSource implements IMixinDamageSource {

    @Unique
    private DamageTypeUtil damageType = null;
    @Unique
    private RiskLevelUtil riskLevel = null;
    @Unique
    private boolean blockable = true;

    @Unique
    @Override
    public IMixinDamageSource damageType(DamageTypeUtil damageType) {
        this.damageType = damageType;
        return this;
    }

    @Unique
    @Override
    public DamageTypeUtil getDamageType() {
        return this.damageType;
    }

    @Unique
    @Override
    public boolean hasDamageType() {
        return this.damageType != null;
    }

    @Unique
    @Override
    public IMixinDamageSource Blockable(boolean blockable) {
        this.blockable = blockable;
        return this;
    }

    @Unique
    @Override
    public boolean isBlockable() {
        return this.blockable;
    }

    @Unique
    @Override
    public IMixinDamageSource riskLevel(RiskLevelUtil riskLevel) {
        this.riskLevel =riskLevel;
        return this;
    }

    @Unique
    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Unique
    @Override
    public boolean hasRiskLevel() {
        return this.riskLevel != null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "mobAttack", at = @At("HEAD"), cancellable = true)
    private static void mobAttack_Head(LivingEntity pMob, CallbackInfoReturnable<DamageSource> cir) {
        if (pMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof EgoMeleeWeapon egoMeleeWeapon) {
            cir.setReturnValue(getAbnormalDamageSource("mob", pMob,
                    egoMeleeWeapon.getRiskLevel(), egoMeleeWeapon.getDamageType(), false));
        }
        if (pMob instanceof AbnormalityEntity abnormality) {
            cir.setReturnValue(getAbnormalDamageSource("mob", pMob,
                    abnormality.getRiskLevel(),
                    abnormality.getDamageType(),
                    !abnormality.canDoUnblockableAttack()));
        }
    }

    @Inject(method = "playerAttack", at = @At("HEAD"), cancellable = true)
    private static void playerAttack_Head(Player pPlayer, CallbackInfoReturnable<DamageSource> cir) {
        if (pPlayer.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof EgoMeleeWeapon egoMeleeWeapon) {
            cir.setReturnValue(getAbnormalDamageSource("player", pPlayer,
                    egoMeleeWeapon.getRiskLevel(), egoMeleeWeapon.getDamageType(), false));
        }
    }

    @Unique
    private static DamageSource getAbnormalDamageSource(String damageTypeId, LivingEntity livingEntity,
                                                        RiskLevelUtil riskLevel, DamageTypeUtil damageType, boolean blockable) {
        EntityDamageSource base = new EntityDamageSource(damageTypeId, livingEntity);
        IMixinDamageSource iMixinDamageSource = ((IMixinDamageSource) base)
                .riskLevel(riskLevel).damageType(damageType).Blockable(blockable);
        return (DamageSource) iMixinDamageSource;
    }
}

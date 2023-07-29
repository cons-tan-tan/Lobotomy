package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;

public abstract class AbnormalityEntity extends Monster implements IRiskLevel {

    protected final AnimationFactory FACTORY;

    protected DamageTypeUtil currentDamageType;

    public AbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        FACTORY = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        this.currentDamageType = this.getDefaultDamageType();
    }

    //サブクラスでIAnimatableを実装した場合に使える
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return ModEntityTypes.abnormalityEntityType(this.getType()).getRiskLevel();
    }

    public DamageSource getAbnormalDamageSource(DamageTypeUtil damageType, boolean blockable) {
        IMixinDamageSource damageSource = ((IMixinDamageSource) (Object) DamageSource.mobAttack(this))
                .riskLevel(this.getRiskLevel()).damageType(this.getDefaultDamageType()).Blockable(blockable);
        return (DamageSource) (Object) damageSource;
    }

    public Map<DamageTypeUtil, Float> getDefense() {
        return ModEntityTypes.abnormalityEntityType(this.getType()).getDefense();
    }

    public DamageTypeUtil getDefaultDamageType() {
        return ModEntityTypes.abnormalityEntityType(this.getType()).getDamageType();
    }

    public DamageTypeUtil getCurrentDamageType() {
        return this.currentDamageType;
    }

    public void setCurrentDamageType(DamageTypeUtil damageType) {
        if (damageType != null) this.currentDamageType = damageType;
    }

    public void resetCurrentDamageType() {
        this.currentDamageType = this.getDefaultDamageType();
    }
}

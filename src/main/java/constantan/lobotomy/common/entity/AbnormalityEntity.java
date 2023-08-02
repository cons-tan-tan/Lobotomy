package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.IDefense;
import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;

public abstract class AbnormalityEntity extends Monster implements IRiskLevel, IDefense {

    protected final AnimationFactory factory;

    private final IMixinEntityType<?> abnormalityType;
    private final IMixinDamageSource abnormalDamageSource;

    protected DamageTypeUtil currentDamageType;

    public AbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        this.abnormalityType = ModEntityTypes.abnormalityEntityType(this.getType());
        this.abnormalDamageSource = (IMixinDamageSource) (Object) DamageSource.mobAttack(this);

        this.currentDamageType = this.getDefaultDamageType();
    }

    //サブクラスでIAnimatableを実装した場合に使える
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public DamageSource getAbnormalDamageSource(DamageTypeUtil damageType, boolean blockable) {
        return (DamageSource) (Object) this.abnormalDamageSource.riskLevel(this.getRiskLevel()).damageType(damageType).Blockable(blockable);
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.abnormalityType.getRiskLevel();
    }

    @Override
    public Map<DamageTypeUtil, Float> getAbnormalDefense() {
        return this.abnormalityType.getDefense();
    }

    public DamageTypeUtil getDefaultDamageType() {
        return this.abnormalityType.getDamageType();
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

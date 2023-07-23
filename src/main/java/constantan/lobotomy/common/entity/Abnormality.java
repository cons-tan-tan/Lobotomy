package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.LivingEntityDefenseUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class Abnormality extends Monster implements IAnimatable {

    public final AnimationFactory FACTORY;

    public Map<DamageTypeUtil, Float> Defense;
    public RiskLevelUtil riskLevel;
    public DamageTypeUtil defaultDamageType;
    public DamageTypeUtil currentDamageType;

    public Abnormality(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.FACTORY = GeckoLibUtil.createFactory(this);

        this.setDefaultDamageType(DamageTypeUtil.RED);
        this.riskLevel = RiskLevelUtil.ZAYIN;
        this.Defense = new HashMap<>(LivingEntityDefenseUtil.DEFAULT_ENTITY_DEFENSE);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        DamageTypeUtil damageType = this.getCurrentDamageType();
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (pEntity instanceof LivingEntity livingEntity) {
            return damageType.hurtLivingEntity(livingEntity, this, f);
        }

        return super.doHurtTarget(pEntity);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    public Map<DamageTypeUtil, Float> getDefense() {
        return this.Defense;
    }

    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    public DamageTypeUtil getDefaultDamageType() {
        return this.defaultDamageType;
    }

    public void setDefaultDamageType(DamageTypeUtil damageType) {
        if (damageType != null) {
            this.defaultDamageType = damageType;
            this.currentDamageType = damageType;
        }
    }

    public DamageTypeUtil getCurrentDamageType() {
        if (this.currentDamageType != null) {
            return this.currentDamageType;
        }
        return this.defaultDamageType;
    }

    public void setCurrentDamageType(DamageTypeUtil damageType) {
        this.currentDamageType = damageType;
    }

    public void resetCurrentDamageType() {
        this.currentDamageType = this.defaultDamageType;
    }
}

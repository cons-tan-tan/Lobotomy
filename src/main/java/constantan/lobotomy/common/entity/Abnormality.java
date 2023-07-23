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

import java.util.HashMap;
import java.util.Map;

public abstract class Abnormality extends Monster {

    public final Map<DamageTypeUtil, Float> Defense;
    public final RiskLevelUtil RISK_LEVEL;
    public final DamageTypeUtil DEFAULT_DAMAGE_TYPE;

    public DamageTypeUtil currentDamageType;

    protected Abnormality(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.DEFAULT_DAMAGE_TYPE = DamageTypeUtil.RED;
        this.RISK_LEVEL = RiskLevelUtil.ZAYIN;
        Defense = new HashMap<>(LivingEntityDefenseUtil.DEFAULT_ENTITY_DEFENSE);
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

    public Map<DamageTypeUtil, Float> getDefense() {
        return this.Defense;
    }

    public RiskLevelUtil getRiskLevel() {
        return this.RISK_LEVEL;
    }

    public DamageTypeUtil getDefaultDamageType() {
        return this.DEFAULT_DAMAGE_TYPE;
    }

    public DamageTypeUtil getCurrentDamageType() {
        return this.currentDamageType;
    }

    public void setCurrentDamageType(DamageTypeUtil damageType) {
        this.currentDamageType = damageType;
    }

    public void resetCurrentDamageType() {
        this.currentDamageType = this.DEFAULT_DAMAGE_TYPE;
    }
}

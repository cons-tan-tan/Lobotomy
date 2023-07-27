package constantan.lobotomy.mixin;

import constantan.lobotomy.common.init.IMixinEntityType;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(EntityType.class)
public abstract class MixinEntityType<T extends Entity> implements IMixinEntityType<T> {

    @Unique
    private RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
    @Unique
    private DamageTypeUtil defaultDamageType = DamageTypeUtil.RED;
    @Unique
    private Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

    @Unique
    @Override
    public IMixinEntityType<T> riskLevel(RiskLevelUtil riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

    @Unique
    @Override
    public IMixinEntityType<T> damageType(DamageTypeUtil defaultDamageType) {
        this.defaultDamageType = defaultDamageType;
        return this;
    }

    @Unique
    @Override
    public IMixinEntityType<T> defense(float red, float white, float black, float pale) {
        this.defense = DefenseUtil.createDefense(red, white, black, pale);
        return this;
    }

    @Unique
    @Override
    public EntityType<T> build() {
        return (EntityType<T>) (Object) this;
    }

    @Unique
    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Unique
    @Override
    public DamageTypeUtil getDamageType() {
        return this.defaultDamageType;
    }

    @Unique
    @Override
    public Map<DamageTypeUtil, Float> getDefense() {
        return this.defense;
    }
}

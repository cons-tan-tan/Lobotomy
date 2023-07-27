package constantan.lobotomy.mixin;

import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
}

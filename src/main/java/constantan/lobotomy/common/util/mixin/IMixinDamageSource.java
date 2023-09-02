package constantan.lobotomy.common.util.mixin;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;

public interface IMixinDamageSource {

    IMixinDamageSource damageType(DamageTypeUtil damageType);

    DamageTypeUtil getDamageType();

    boolean hasDamageType();

    IMixinDamageSource Blockable(boolean blockable);

    boolean isBlockable();

    IMixinDamageSource ignoreInvulnerable();

    boolean canIgnoreInvulnerable();

    IMixinDamageSource riskLevel(RiskLevelUtil riskLevel);

    RiskLevelUtil getRiskLevel();

    boolean hasRiskLevel();
}

package constantan.lobotomy.common.init;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;

public interface IMixinDamageSource {

    IMixinDamageSource damageType(DamageTypeUtil damageType);

    DamageTypeUtil getDamageType();

    boolean hasDamageType();

    IMixinDamageSource Blockable(boolean blockable);

    boolean isBlockable();

    IMixinDamageSource riskLevel(RiskLevelUtil riskLevel);

    RiskLevelUtil getRiskLevel();

    boolean hasRiskLevel();
}

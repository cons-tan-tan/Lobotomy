package constantan.lobotomy.common.util;

public interface IRiskLevel {

    RiskLevelUtil getRiskLevel();

    default RiskLevelUtil getDisplayRiskLevel() {
        return this.getRiskLevel();
    }
}

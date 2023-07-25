package constantan.lobotomy.common.util;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public enum RiskLevelUtil {
    ZAYIN,
    TETH,
    HE,
    WAW,
    ALEPH;

    public int getLeveInt() {
        return switch (this) {
            case ZAYIN -> 1;
            case TETH -> 2;
            case HE -> 3;
            case WAW -> 4;
            case ALEPH -> 5;
        };
    }

    public static float getDamageRatio(RiskLevelUtil defenderRiskLevel, RiskLevelUtil attackerRiskLevel) {
        return switch (defenderRiskLevel.getLeveInt() - attackerRiskLevel.getLeveInt()) {
            case 4 -> 0.4F;
            case 3 -> 0.6F;
            case 2 -> 0.7F;
            case 1 -> 0.8F;
            default -> 1.0F;
            case -2 -> 1.2F;
            case -3 -> 1.5F;
            case -4 -> 2.0F;
        };
    }

    public static float getDamageRatio(LivingEntity defender, LivingEntity attacker) {
        return getDamageRatio(getRiskLevel(defender), getRiskLevel(attacker));
    }

    public static RiskLevelUtil getRiskLevel(LivingEntity livingEntity) {
        if (livingEntity instanceof AbnormalityEntity abnormalityEntity) {
            return abnormalityEntity.getRiskLevel();
        } else if (livingEntity instanceof Player player) {

            //PlayerのEGOのランクを取得

        }
        return getLivingEntityRiskLevel(livingEntity);
    }

    private static RiskLevelUtil getLivingEntityRiskLevel(LivingEntity livingEntity) {
        return ZAYIN;
    }
}

package constantan.lobotomy.common.item;

import constantan.lobotomy.common.util.IAbnormalityTool;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.item.Item;

public abstract class AbnormalityToolItem extends Item implements IAbnormalityTool {

    public final RiskLevelUtil riskLevel;

    public AbnormalityToolItem(Item.Properties pProperties) {
        super(pProperties);

        AbnormalityToolItemProperties abnormalityToolItemProperties = (AbnormalityToolItemProperties) pProperties;
        this.riskLevel = abnormalityToolItemProperties.riskLevel;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    public static class AbnormalityToolItemProperties extends Item.Properties {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;

        public AbnormalityToolItemProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }
    }
}

package constantan.lobotomy.common.item.util;

import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.item.Item;

public interface IEgo extends IRiskLevel {

    class EgoProperties extends Item.Properties {

        public RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;

        public EgoProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }
    }
}

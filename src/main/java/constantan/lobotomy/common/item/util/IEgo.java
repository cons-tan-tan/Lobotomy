package constantan.lobotomy.common.item.util;

import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.item.Item;

public interface IEgo extends IRiskLevel {

    abstract class EgoProperties extends Item.Properties {

        public abstract EgoProperties riskLevel(RiskLevelUtil riskLevel);

        public abstract EgoProperties idleAnim();
    }
}

package constantan.lobotomy.common.item;

import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

public class AbnormalitySpawnEggItem extends ForgeSpawnEggItem implements IRiskLevel {

    public AbnormalitySpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor) {
        super(type, backgroundColor, highlightColor, new Properties().tab(ModSetup.CREATIVE_TAB));
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return ((IMixinEntityType<? extends Entity>) this.getType(null)).getRiskLevel();
    }
}

package constantan.lobotomy.common.item;

import constantan.lobotomy.common.ModSetup;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EgoGift extends Item implements ICurioItem {

    public EgoGift(Properties pProperties) {
        super(pProperties.stacksTo(1).tab(ModSetup.CREATIVE_TAB));
    }
}

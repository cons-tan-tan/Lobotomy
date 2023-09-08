package constantan.lobotomy.common.item;

import constantan.lobotomy.common.ModSetup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EgoGift extends Item implements ICurioItem {

    public EgoGift(Properties pProperties) {
        super(pProperties.stacksTo(1).tab(ModSetup.CREATIVE_TAB));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}

package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.renderer.item.PeakWeaponRenderer;
import constantan.lobotomy.common.init.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ModItemRenderers<T extends Item> {

    private static final Map<Item, BlockEntityWithoutLevelRenderer> map = new HashMap<>();

    public static BlockEntityWithoutLevelRenderer getRenderer(Item item) {
        return map.get(item);
    }

    static {
        map.put(ModItems.PEAK_WEAPON.get(), new PeakWeaponRenderer());
    }
}

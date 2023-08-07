package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.renderer.item.EgoWeaponItemRenderer;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ModItemRenderers<T extends Item> {

    private static final Map<Item, BlockEntityWithoutLevelRenderer> MAP = new HashMap<>();

    public static BlockEntityWithoutLevelRenderer getRenderer(Item item) {
        return MAP.get(item);
    }

    static {
        MAP.put(ModItems.PEAK_WEAPON.get(), new EgoWeaponItemRenderer<>(LibEntityResources.PUNISHING_BIRD));
        MAP.put(ModItems.HEAVEN_WEAPON.get(), new EgoWeaponItemRenderer<>(LibEntityResources.THE_BURROWING_HEAVEN));
    }
}

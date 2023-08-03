package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.renderer.item.EgoWeaponItemRenderer;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibEntityResources;
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
        map.put(ModItems.PEAK_WEAPON.get(), new EgoWeaponItemRenderer<>(LibEntityResources.PUNISHING_BIRD));
        map.put(ModItems.HEAVEN_WEAPON.get(), new EgoWeaponItemRenderer<>(LibEntityResources.THE_BURROWING_HEAVEN));
    }
}

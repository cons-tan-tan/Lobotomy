package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.renderer.armor.EgoArmorRenderer;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorRenderers {

    private static final Map<Item, Supplier<EgoArmorRenderer>> map = new HashMap<>();

    public static void register() {
        for (Item key : map.keySet()) {
            EgoArmorRenderer.registerEgoArmorRenderer(key, map.get(key));
        }
    }

    static {
        map.put(ModItems.HEAVEN_ARMOR.get(), () -> new EgoArmorRenderer<>(LibEntityResources.THE_BURROWING_HEAVEN));
    }
}

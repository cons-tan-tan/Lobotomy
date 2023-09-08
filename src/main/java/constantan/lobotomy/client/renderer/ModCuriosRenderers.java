package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.model.gift.EgoGiftModelPart;
import constantan.lobotomy.client.model.gift.HeavenGiftModel;
import constantan.lobotomy.client.renderer.gift.CrownGiftRenderer;
import constantan.lobotomy.client.renderer.gift.SkinOverlayGiftRenderer;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModCuriosRenderers {

    private static final Map<Item, Supplier<ICurioRenderer>> MAP = new HashMap<>();

    public static void register() {
        MAP.forEach(CuriosRendererRegistry::register);
    }

    static {
        MAP.put(ModItems.JUSTITIA_GIFT.get(), () ->
                new SkinOverlayGiftRenderer(LibAbnormality.JUDGEMENT_BIRD.getGiftEgoTexture(), EgoGiftModelPart.HEAD));
        MAP.put(ModItems.HEAVEN_GIFT.get(), () ->
                new CrownGiftRenderer(LibAbnormality.THE_BURROWING_HEAVEN.getGiftEgoTexture(), new HeavenGiftModel<>()));
    }
}

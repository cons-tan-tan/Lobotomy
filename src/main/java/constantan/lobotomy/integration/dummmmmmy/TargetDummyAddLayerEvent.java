package constantan.lobotomy.integration.dummmmmmy;

import constantan.lobotomy.client.renderer.entity.layer.EgoSuitLayer;
import constantan.lobotomy.lib.LibMisc;
import net.mehvahdjukaar.dummmmmmy.client.TargetDummyRenderer;
import net.mehvahdjukaar.dummmmmmy.setup.ModRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetDummyAddLayerEvent {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SubscribeEvent
    public static void onAddLayer(EntityRenderersEvent.AddLayers event) {
        if (ModList.get().isLoaded("dummmmmmy")) {
            LivingEntityRenderer renderer = event.getRenderer(ModRegistry.TARGET_DUMMY.get());
            if (renderer instanceof TargetDummyRenderer) {
                renderer.addLayer(new EgoSuitLayer(renderer, new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER), false)));
            }
        }
    }
}

package constantan.lobotomy.client;

import constantan.lobotomy.client.gui.SanityHudOverlay;
import constantan.lobotomy.client.key.KeyBindings;
import constantan.lobotomy.client.key.KeyInputHandler;
import constantan.lobotomy.client.renderer.entity.layer.EgoSuitLayer;
import constantan.lobotomy.client.renderer.ModArmorRenderers;
import constantan.lobotomy.client.renderer.ModEntityRenderers;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ModEntityRenderers.register();
        ModArmorRenderers.register();

        MinecraftForge.EVENT_BUS.addListener(KeyInputHandler::onKeyInput);
        KeyBindings.init();
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "sanity", SanityHudOverlay.HUD_SANITY);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayer(event, "slim");
        addPlayerLayer(event, "default");
        LivingEntityRenderer renderer = event.getRenderer(EntityType.ARMOR_STAND);
        if (renderer instanceof ArmorStandRenderer) {
            renderer.addLayer(new EgoSuitLayer(renderer, new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER), false)));
        }
    }

    private static void addPlayerLayer(EntityRenderersEvent.AddLayers event, String skin) {
        EntityRenderer<? extends Player> renderer = event.getSkin(skin);
        if (renderer instanceof LivingEntityRenderer livingRenderer) {
            boolean isSlim = skin.equals("slim");
            livingRenderer.addLayer(new EgoSuitLayer(livingRenderer, new PlayerModel<>(event.getEntityModels().bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), isSlim)));
        }
    }
}

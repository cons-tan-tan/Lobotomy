package constantan.lobotomy.client;

import constantan.lobotomy.client.gui.SanityHudOverlay;
import constantan.lobotomy.client.key.KeyBindings;
import constantan.lobotomy.client.key.KeyInputHandler;
import constantan.lobotomy.client.renderer.ModEntityRenderers;
import constantan.lobotomy.lib.LibMisc;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ModEntityRenderers.register();

        MinecraftForge.EVENT_BUS.addListener(KeyInputHandler::onKeyInput);
        KeyBindings.init();
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "sanity", SanityHudOverlay.HUD_SANITY);
    }
}

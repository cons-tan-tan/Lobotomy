package constantan.lobotomy.client;

import constantan.lobotomy.client.renderer.entity.PunishingBirdRenderer;
import constantan.lobotomy.common.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdRenderer::new);

        KeyBindings.init();
    }
}

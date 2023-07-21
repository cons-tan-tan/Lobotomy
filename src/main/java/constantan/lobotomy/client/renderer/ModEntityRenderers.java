package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.renderer.entity.PunishingBirdRenderer;
import constantan.lobotomy.common.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class ModEntityRenderers {
    public static void register() {
        EntityRenderers.register(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdRenderer::new);
    }
}

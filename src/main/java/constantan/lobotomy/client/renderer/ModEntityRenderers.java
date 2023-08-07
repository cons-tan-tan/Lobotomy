package constantan.lobotomy.client.renderer;

import constantan.lobotomy.client.model.entity.PunishingBirdModel;
import constantan.lobotomy.client.renderer.entity.AbnormalityEntityRenderer;
import constantan.lobotomy.client.renderer.entity.TheBurrowingHeavenRenderer;
import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class ModEntityRenderers {

    private static final Map<EntityType<? extends Entity>, EntityRendererProvider> MAP = new HashMap<>();

    public static void register() {
        MAP.forEach(EntityRenderers::register);
    }

    static {
        MAP.put(ModEntityTypes.PUNISHING_BIRD.get(),
                r -> new AbnormalityEntityRenderer<>(r, new PunishingBirdModel(LibEntityResources.PUNISHING_BIRD), 0.3F, 0.4F));
        MAP.put(ModEntityTypes.THE_BURROWING_HEAVEN.get(),
                TheBurrowingHeavenRenderer::new);
    }
}

package constantan.lobotomy.lib;

import net.minecraft.resources.ResourceLocation;

public class LibEntityResources {

    public static ResourceLocation path(String path) {
        return new ResourceLocation(LibMisc.MOD_ID, path);
    }

    public static final String PUNISHING_BIRD_NAME = "punishing_bird";
    public static final String PUNISHING_BIRD_BUILD = path(PUNISHING_BIRD_NAME).toString();
    public static final ResourceLocation PUNISHING_BIRD_MODEL = path("geo/entity/punishing_bird.geo.json");
    public static final ResourceLocation PUNISHING_BIRD_TEXTURE_NORMAL = path("textures/entity/punishing_bird_normal.png");
    public static final ResourceLocation PUNISHING_BIRD_TEXTURE_ANGRY = path("textures/entity/punishing_bird_angry.png");
    public static final ResourceLocation PUNISHING_BIRD_ANIMATION = path("animations/entity/punishing_bird.animation.json");
}

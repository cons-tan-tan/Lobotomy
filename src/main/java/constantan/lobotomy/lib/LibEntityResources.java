package constantan.lobotomy.lib;

import net.minecraft.resources.ResourceLocation;

public class LibEntityResources {

    public static final EntityResourceData PUNISHING_BIRD = new EntityResourceData("punishing_bird");
    public static final EntityResourceData THE_BURROWING_HEAVEN = new EntityResourceData("the_burrowing_heaven");


    public static class EntityResourceData {

        private final String NAME;

        public EntityResourceData(String name) {
            this.NAME = name;
        }

        public static ResourceLocation path(String path) {
            return new ResourceLocation(LibMisc.MOD_ID, path);
        }

        public String getName() {
            return this.NAME;
        }

        public String getBuild() {
            return path(this.NAME).toString();
        }

        public ResourceLocation getModel() {
            return path("geo/entity/" + this.NAME + ".geo.json");
        }

        public ResourceLocation getAnimation() {
            return path("animations/entity/" + this.NAME + ".animation.json");
        }

        public ResourceLocation getTexture() {
            return path("textures/entity/" + this.NAME + ".png");
        }

        public ResourceLocation getTexture(String key) {
            return path("textures/entity/" + this.NAME + "_" + key + ".png");
        }
    }
}

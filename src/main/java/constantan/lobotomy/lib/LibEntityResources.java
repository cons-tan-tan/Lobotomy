package constantan.lobotomy.lib;

import net.minecraft.resources.ResourceLocation;

public class LibEntityResources {

    public static final EntityResourceData PUNISHING_BIRD = new EntityResourceData("punishing_bird");
    public static final EntityResourceData THE_BURROWING_HEAVEN = new EntityResourceData("the_burrowing_heaven");


    public static class EntityResourceData {

        private final String name;

        public EntityResourceData(String name) {
            this.name = name;
        }

        public static ResourceLocation path(String path) {
            return new ResourceLocation(LibMisc.MOD_ID, path);
        }

        public String getName() {
            return this.name;
        }

        public String getSpawnEggName() {
            return this.name + "_spawn_egg";
        }

        public String getBuild() {
            return path(this.name).toString();
        }

        public ResourceLocation getModel() {
            return path("geo/entity/" + this.name + ".geo.json");
        }

        public ResourceLocation getAnimation() {
            return path("animations/entity/" + this.name + ".animation.json");
        }

        public ResourceLocation getTexture() {
            return path("textures/entity/" + this.name + ".png");
        }

        public ResourceLocation getTexture(String key) {
            return path("textures/entity/" + this.name + "_" + key + ".png");
        }
    }
}

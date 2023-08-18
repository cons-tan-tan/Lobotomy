package constantan.lobotomy.lib;

import net.minecraft.resources.ResourceLocation;

public class LibEntityResources {

    public static final EntityResourceData PUNISHING_BIRD = new EntityResourceData("punishing_bird").egoName("peak");
    public static final EntityResourceData JUDGEMENT_BIRD = new EntityResourceData("judgement_bird").egoName("justitia");
    public static final EntityResourceData THE_BURROWING_HEAVEN = new EntityResourceData("the_burrowing_heaven").egoName("heaven");


    public static class EntityResourceData {

        private final String name;
        private String egoName;

        public EntityResourceData(String name) {
            this.name = name;
        }

        EntityResourceData egoName(String egoName) {
            this.egoName = egoName;
            return this;
        }

        private static ResourceLocation path(String path) {
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

        public String getActualEgoName() {
            return this.egoName;
        }

        public String getWeaponEgoName() {
            return this.egoName + "_weapon";
        }

        public ResourceLocation getWeaponEgoModel() {
            return path("geo/item/" + this.egoName + "_weapon.geo.json");
        }

        public ResourceLocation getWeaponEgoAnimation() {
            return path("animations/item/" + this.egoName + "_weapon.animation.json");
        }

        public ResourceLocation getWeaponEgoTexture() {
            return path("textures/item/" + this.egoName + "_weapon.png");
        }

        public String getArmorEgoName() {
            return this.egoName + "_armor";
        }

        public ResourceLocation getArmorEgoModel() {
            return path("geo/item/" + this.egoName + "_armor.geo.json");
        }

        public ResourceLocation getArmorEgoAnimation() {
            return path("animations/item/" + this.egoName + "_armor.animation.json");
        }

        public ResourceLocation getArmorEgoTexture() {
            return path("textures/armor/" + this.egoName + "_armor.png");
        }

        public ResourceLocation getSuitEgoTexture() {
            return path("textures/armor/" + this.egoName + "_suit.png");
        }
    }
}

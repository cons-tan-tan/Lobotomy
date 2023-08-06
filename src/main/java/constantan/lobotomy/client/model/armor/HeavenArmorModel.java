package constantan.lobotomy.client.model.armor;

import constantan.lobotomy.common.item.HeavenArmor;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HeavenArmorModel extends AnimatedGeoModel<HeavenArmor> {
    @Override
    public ResourceLocation getModelLocation(HeavenArmor object) {
        return new ResourceLocation(LibMisc.MOD_ID, "geo/item/heaven_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HeavenArmor object) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/item/heaven_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HeavenArmor animatable) {
        return new ResourceLocation(LibMisc.MOD_ID, "animation/item/heaven_armor.animation.json");
    }
}

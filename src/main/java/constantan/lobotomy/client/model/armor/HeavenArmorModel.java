package constantan.lobotomy.client.model.armor;

import constantan.lobotomy.common.item.HeavenArmorItem;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HeavenArmorModel extends AnimatedGeoModel<HeavenArmorItem> {
    @Override
    public ResourceLocation getModelLocation(HeavenArmorItem object) {
        return new ResourceLocation(LibMisc.MOD_ID, "geo/item/heaven_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HeavenArmorItem object) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/item/heaven_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HeavenArmorItem animatable) {
        return new ResourceLocation(LibMisc.MOD_ID, "animation/item/heaven_armor.animation.json");
    }
}

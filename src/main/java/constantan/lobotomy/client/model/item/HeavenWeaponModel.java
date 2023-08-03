package constantan.lobotomy.client.model.item;

import constantan.lobotomy.common.item.HeavenWeaponItem;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HeavenWeaponModel extends AnimatedGeoModel<HeavenWeaponItem> {
    @Override
    public ResourceLocation getModelLocation(HeavenWeaponItem object) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getWeaponEgoModel();
    }

    @Override
    public ResourceLocation getTextureLocation(HeavenWeaponItem object) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getWeaponEgoTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HeavenWeaponItem animatable) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getWeaponEgoAnimation();
    }
}

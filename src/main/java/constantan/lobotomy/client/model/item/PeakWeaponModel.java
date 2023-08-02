package constantan.lobotomy.client.model.item;

import constantan.lobotomy.common.item.PeakWeaponItem;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PeakWeaponModel extends AnimatedGeoModel<PeakWeaponItem> {
    @Override
    public ResourceLocation getModelLocation(PeakWeaponItem object) {
        return LibEntityResources.PUNISHING_BIRD.getWeaponEgoModel();
    }

    @Override
    public ResourceLocation getTextureLocation(PeakWeaponItem object) {
        return LibEntityResources.PUNISHING_BIRD.getWeaponEgoTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PeakWeaponItem animatable) {
        return LibEntityResources.PUNISHING_BIRD.getWeaponEgoAnimation();
    }
}

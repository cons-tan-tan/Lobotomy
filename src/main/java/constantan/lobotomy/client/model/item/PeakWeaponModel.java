package constantan.lobotomy.client.model.item;

import constantan.lobotomy.common.item.PeakWeaponItem;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PeakWeaponModel extends AnimatedGeoModel<PeakWeaponItem> {
    @Override
    public ResourceLocation getModelLocation(PeakWeaponItem object) {
        return new ResourceLocation(LibMisc.MOD_ID, "geo/item/peak_weapon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PeakWeaponItem object) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/item/peak_weapon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PeakWeaponItem animatable) {
        return new ResourceLocation(LibMisc.MOD_ID, "animations/item/peak_weapon.animation.json");
    }
}

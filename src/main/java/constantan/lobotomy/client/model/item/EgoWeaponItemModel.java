package constantan.lobotomy.client.model.item;

import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EgoWeaponItemModel<T extends Item & IAnimatable> extends AnimatedGeoModel<T> {

    protected final LibAbnormality.EntityResourceData entityResourceData;

    public EgoWeaponItemModel(LibAbnormality.EntityResourceData entityResourceData) {
        super();
        this.entityResourceData = entityResourceData;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return this.entityResourceData.getWeaponEgoModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.entityResourceData.getWeaponEgoTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return this.entityResourceData.getWeaponEgoAnimation();
    }
}

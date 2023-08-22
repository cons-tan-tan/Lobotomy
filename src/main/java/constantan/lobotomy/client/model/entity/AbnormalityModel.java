package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AbnormalityModel<T extends Entity & IAnimatable> extends AnimatedGeoModel<T> {

    protected final LibAbnormality.EntityResourceData entityResourceData;

    public AbnormalityModel(LibAbnormality.EntityResourceData entityResourceData) {
        super();
        this.entityResourceData = entityResourceData;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return this.entityResourceData.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.entityResourceData.getTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return this.entityResourceData.getAnimation();
    }
}

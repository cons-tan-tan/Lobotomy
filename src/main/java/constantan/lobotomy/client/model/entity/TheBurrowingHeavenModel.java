package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TheBurrowingHeavenModel extends AnimatedGeoModel<TheBurrowingHeavenEntity> {

    @Override
    public ResourceLocation getModelLocation(TheBurrowingHeavenEntity object) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(TheBurrowingHeavenEntity object) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TheBurrowingHeavenEntity animatable) {
        return LibEntityResources.THE_BURROWING_HEAVEN.getAnimation();
    }
}

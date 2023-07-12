package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PunishingBirdModel extends AnimatedGeoModel<PunishingBirdEntity> {

    @Override
    public ResourceLocation getModelLocation(PunishingBirdEntity object) {
        return LibEntityResources.PUNISHING_BIRD_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(PunishingBirdEntity object) {
        if (object.isAngry()) return LibEntityResources.PUNISHING_BIRD_TEXTURE_ANGRY;
        return LibEntityResources.PUNISHING_BIRD_TEXTURE_NORMAL;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PunishingBirdEntity animatable) {
        return LibEntityResources.PUNISHING_BIRD_ANIMATION;
    }
}

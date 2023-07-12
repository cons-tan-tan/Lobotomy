package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PunishingBirdModel extends AnimatedGeoModel<PunishingBirdEntity> {

    @Override
    public ResourceLocation getModelLocation(PunishingBirdEntity object) {
        return new ResourceLocation(LibMisc.MOD_ID, "geo/entity/punishing_bird.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PunishingBirdEntity object) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/entity/punishing_bird.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PunishingBirdEntity animatable) {
        return new ResourceLocation(LibMisc.MOD_ID, "animations/entity/punishing_bird.animation.json");
    }
}

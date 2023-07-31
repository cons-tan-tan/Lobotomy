package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
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

    @Override
    public void setCustomAnimations(TheBurrowingHeavenEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        IBone bone = this.getAnimationProcessor().getBone("bone");
        IBone eye = this.getAnimationProcessor().getBone("eye");

        float partialTick = animationEvent.getPartialTick();

        bone.setRotationY(animatable.getYRadForAnimation(partialTick));
        eye.setRotationX(animatable.getXRadForAnimation(partialTick));
    }
}

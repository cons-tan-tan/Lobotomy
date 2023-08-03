package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.lib.LibEntityResources;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class TheBurrowingHeavenModel extends AbnormalityEntityModel<TheBurrowingHeavenEntity> {

    public TheBurrowingHeavenModel(LibEntityResources.EntityResourceData entityResourceData) {
        super(entityResourceData);
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

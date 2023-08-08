package constantan.lobotomy.common.item.ego;

import constantan.lobotomy.common.item.EgoArmor;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class SimpleEgoArmorItem extends EgoArmor implements IAnimatable {

    private final boolean hasIdleAnim;

    public SimpleEgoArmorItem(Properties builder) {
        super(builder);

        SimpleEgoArmorProperties simpleEgoArmorProperties = (SimpleEgoArmorProperties) builder;
        this.hasIdleAnim = simpleEgoArmorProperties.hasIdleAnim;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::predicate));
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.hasIdleAnim) {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    public static class SimpleEgoArmorProperties extends EgoArmorProperties {

        boolean hasIdleAnim;

        public SimpleEgoArmorProperties idleAnim() {
            this.hasIdleAnim = true;
            return this;
        }
    }
}

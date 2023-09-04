package constantan.lobotomy.common.item.ego;

import constantan.lobotomy.common.item.EgoRangeWeapon;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SimpleEgoRangeWeapon extends EgoRangeWeapon implements IAnimatable, ISyncable {

    protected static final AnimationBuilder ANIM_FIRE = new AnimationBuilder()
            .addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    protected static final String ANIM_CONTROLLER_NAME = "controller";

    public SimpleEgoRangeWeapon(int minDamage, int maxDamage, Properties pProperties) {
        super(minDamage, maxDamage, pProperties);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, ANIM_CONTROLLER_NAME, 1, event -> PlayState.CONTINUE));
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.getFactory(), id, ANIM_CONTROLLER_NAME);
        controller.markNeedsReload();
        if (state == FIRE_ANIM_STATE) {
            controller.clearAnimationCache();
            controller.setAnimation(ANIM_FIRE);
        }
    }
}

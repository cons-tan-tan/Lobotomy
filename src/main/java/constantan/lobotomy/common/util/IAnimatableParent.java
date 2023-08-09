package constantan.lobotomy.common.util;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;

public interface IAnimatableParent {

    AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);

    /**
     * {@link IAnimatable}専用<br>
     * {@link IAnimatable#registerControllers}で登録した{@link AnimationController}を
     * {@link ISyncable#onAnimationSync}で制御する場合transitionLengthTicksを1以上にしてないとクラッシュするので注意
     */
    AnimationFactory getFactory();
}

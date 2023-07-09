package constantan.lobotomy.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Predicate;

public class PeakWeaponItem extends ProjectileWeaponItem implements IAnimatable, ISyncable {

    protected static final AnimationBuilder IDLE = new AnimationBuilder()
            .addAnimation("animation.peak_weapon.idle", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    protected static final AnimationBuilder FIRE = new AnimationBuilder()
            .addAnimation("animation.peak_weapon.fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private final AnimationFactory FACTORY = GeckoLibUtil.createFactory(this);

    public final int FIRE_ANIM_STATE = 0;

    public final String ANIM_CONTROLLER_NAME = "controller";

    public PeakWeaponItem(Properties pProperties) {
        super(pProperties);
    }

    private PlayState predicate(AnimationEvent event) {
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, ANIM_CONTROLLER_NAME, 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == FIRE_ANIM_STATE) {
            AnimationController controller = GeckoLibUtil.getControllerForID(this.FACTORY, id, ANIM_CONTROLLER_NAME);
            if (controller.getAnimationState() == AnimationState.Stopped) {

            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }
}

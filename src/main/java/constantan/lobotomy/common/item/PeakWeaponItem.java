package constantan.lobotomy.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PeakWeaponItem extends EgoRangeWeaponItem implements IAnimatable, ISyncable {

    protected static final AnimationBuilder FIRE = new AnimationBuilder()
            .addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public final String ANIM_CONTROLLER_NAME = "controller";
    public final int FIRE_ANIM_STATE = 0;

    public PeakWeaponItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, ANIM_CONTROLLER_NAME, 1, (event -> PlayState.CONTINUE)));
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.getFactory(), id, ANIM_CONTROLLER_NAME);
        controller.markNeedsReload();
        if (state == FIRE_ANIM_STATE) {
            controller.clearAnimationCache();
            controller.setAnimation(FIRE);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            final ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) pLevel);
            GeckoLibNetwork.syncAnimation(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pPlayer), this, id, FIRE_ANIM_STATE);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}

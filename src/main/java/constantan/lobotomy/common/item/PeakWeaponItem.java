package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.item.PeakWeaponRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class PeakWeaponItem extends Item implements IAnimatable, ISyncable {

    protected static final AnimationBuilder FIRE = new AnimationBuilder()
            .addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private final AnimationFactory FACTORY = GeckoLibUtil.createFactory(this);

    public final int FIRE_ANIM_STATE = 0;

    public final String ANIM_CONTROLLER_NAME = "controller";

    public PeakWeaponItem(Properties pProperties) {
        super(pProperties);
        GeckoLibNetwork.registerSyncable(this);
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        //onAnimationSyncで制御するAnimationControllerはtransitionLengthTicksを1以上にしてないとクラッシュする!!!!
        data.addAnimationController(new AnimationController<>(this, ANIM_CONTROLLER_NAME, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == FIRE_ANIM_STATE) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.FACTORY, id, ANIM_CONTROLLER_NAME);
            controller.markNeedsReload();
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

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new PeakWeaponRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}

package constantan.lobotomy.common.item.ego;

import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.item.EgoRangeWeapon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PeakWeaponItem extends EgoRangeWeapon implements IAnimatable, ISyncable {

    protected static final AnimationBuilder ANIM_FIRE = new AnimationBuilder()
            .addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public static final String ANIM_CONTROLLER_NAME = "controller";
    public static final int FIRE_ANIM_STATE = 0;

    public PeakWeaponItem(int minDamage, int maxDamage, Properties pProperties) {
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

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            this.playAnimation(pPlayer, pUsedHand, FIRE_ANIM_STATE);
            float range = 16;
            Vec3 eyePos = pPlayer.getEyePosition();
            Vec3 rangeVec = pPlayer.getViewVector(1.0F).normalize().scale(range);
            AABB searchArea = pPlayer.getBoundingBox().expandTowards(rangeVec).inflate(1.0F);
            EntityHitResult result = ProjectileUtil
                    .getEntityHitResult(pPlayer, eyePos, eyePos.add(rangeVec), searchArea,
                            target -> !target.isSpectator() && target.isPickable(), Math.pow(range, 2));
            if (result != null) {
                LobotomyMod.logger.info("distance = " + result.getEntity().distanceTo(pPlayer));
                result.getEntity().hurt(DamageSource.playerAttack(pPlayer), this.getRangedRandomDamage(stack));
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}

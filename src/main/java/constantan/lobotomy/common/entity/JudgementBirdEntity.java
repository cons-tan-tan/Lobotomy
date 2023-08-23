package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;

public class JudgementBirdEntity extends AbnormalityEntity implements IAnimatable {

    private static final AnimationBuilder ANIM_ATTACK = new AnimationBuilder()
            .addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private static final int ATTACK_DAMAGE_RANDOM_RANGE = 10;

    public JudgementBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new JudgeMentBirdLookControl(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller",
                0, this::predicate));
    }

    private PlayState predicate(AnimationEvent<JudgementBirdEntity> event) {
        if (this.swinging) {
            event.getController().setAnimation(ANIM_ATTACK);
        } else if (this.isOnGround() && event.isMoving()) {
            event.getController().setAnimation(ANIM_WALK);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, true, true, livingEntity ->
                livingEntity instanceof Player));
    }

    @Override
    public double getAttributeValue(Attribute pAttribute) {
        if (pAttribute == Attributes.ATTACK_DAMAGE) {
            return super.getAttributeValue(pAttribute) + new Random().nextInt(ATTACK_DAMAGE_RANDOM_RANGE + 1);
        }
        return super.getAttributeValue(pAttribute);
    }

    @Override
    public boolean canDoUnblockableAttack() {
        return true;
    }

    @Override
    public boolean canDoKnockbackAttack() {
        return false;
    }

    private static class JudgeMentBirdLookControl extends LookControl {

        public JudgeMentBirdLookControl(Mob pMob) {
            super(pMob);
        }

        @Override
        public void setLookAt(double pX, double pY, double pZ, float pDeltaYaw, float pDeltaPitch) {
            super.setLookAt(pX, pY, pZ, pDeltaYaw, pDeltaPitch);
            this.yMaxRotSpeed = Math.min(this.yMaxRotSpeed, 100.0F);
        }
    }
}

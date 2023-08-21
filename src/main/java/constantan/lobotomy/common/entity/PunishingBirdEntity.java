package constantan.lobotomy.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;
import java.util.UUID;

public class PunishingBirdEntity extends AbnormalityEntity implements IAnimatable {

    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(PunishingBirdEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(PunishingBirdEntity.class, EntityDataSerializers.INT);

    private static final AnimationBuilder ANIM_ATTACK_NORMAL = new AnimationBuilder()
            .addAnimation("attack_normal", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private static final AnimationBuilder ANIM_ATTACK_ANGRY = new AnimationBuilder()
            .addAnimation("attack_angry", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private static final UUID MAX_HEALTH_MODIFIER_UUID = UUID.fromString("7354db20-6023-4192-a616-086dc218bc96");
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("e7c57c66-49f4-4a35-9f37-1be7b2e06952");
    private static final UUID FLYING_SPEED_MODIFIER_UUID = UUID.fromString("06a45601-13c7-434c-ac6f-941727e48e35");

    private static final String ATTRIBUTE_MODIFIER_NAME = "Punishing Bird Modifier";

    private static final AttributeModifier FLYING_SPEED_MODIFIER = new AttributeModifier(
            FLYING_SPEED_MODIFIER_UUID, ATTRIBUTE_MODIFIER_NAME, -Float.MAX_VALUE, AttributeModifier.Operation.ADDITION);

    public static final float NORMAL_MAX_HEALTH = 200.0F;
    public static final float ANGRY_MAX_HEALTH = 1000.0F;

    public static final double NORMAL_ATTACK_DAMAGE = 1.0D;
    public static final double ANGRY_ATTACK_DAMAGE = 1000.0D;

    public static final int WAIT_ANIMATION_TICK = 30;
    public static final int ENOUGH_REST_TICK = 20 * 60;

    private int restTick = 0;

    public PunishingBirdEntity(EntityType<? extends AbnormalityEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 0, false);
        this.lookControl = new PunishingBirdLookControl(this);
    }

    public void anger() {
        double attackDamageModifierValue = ANGRY_ATTACK_DAMAGE - this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        AttributeModifier attackDamageModifier = new AttributeModifier(
                ATTACK_DAMAGE_MODIFIER_UUID, ATTRIBUTE_MODIFIER_NAME, attackDamageModifierValue, AttributeModifier.Operation.ADDITION);
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(attackDamageModifier);

        double maxHealthModifierValue = ANGRY_MAX_HEALTH - this.getAttributeValue(Attributes.MAX_HEALTH);
        AttributeModifier maxHealthModifier = new AttributeModifier(
                MAX_HEALTH_MODIFIER_UUID, ATTRIBUTE_MODIFIER_NAME, maxHealthModifierValue, AttributeModifier.Operation.ADDITION);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(maxHealthModifier);
        this.setHealth(this.getHealth() + (ANGRY_MAX_HEALTH - NORMAL_MAX_HEALTH));

        this.setAngry(true);
    }

    public void calmDown() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).removePermanentModifier(ATTACK_DAMAGE_MODIFIER_UUID);
        this.getAttribute(Attributes.MAX_HEALTH).removePermanentModifier(MAX_HEALTH_MODIFIER_UUID);
        this.setHealth(Math.min(this.getHealth(), this.getMaxHealth()));
        this.setAngry(false);
    }

    public void stopFlyingSpeed() {
        AttributeInstance flyingSpeed = this.getAttribute(Attributes.FLYING_SPEED);
        if (!flyingSpeed.hasModifier(FLYING_SPEED_MODIFIER)) {
            flyingSpeed.addPermanentModifier(FLYING_SPEED_MODIFIER);
        }
    }

    public void resetFlyingSpeed() {
        AttributeInstance flyingSpeed = this.getAttribute(Attributes.FLYING_SPEED);
        if (flyingSpeed.hasModifier(FLYING_SPEED_MODIFIER)) {
            flyingSpeed.removePermanentModifier(FLYING_SPEED_MODIFIER_UUID);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == DamageSource.FALL) return false;
        boolean flag = super.hurt(pSource, pAmount);
        if (flag && this.getHealth() > 0) {
            this.resetRestTick();
            if (!this.isAngry() && !pSource.isBypassInvul()) {
                this.anger();
            }
        }
        return flag;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            this.setAttackTick(Math.max(this.getAttackTick() - 1, 0));
            if (this.getAttackTick() == 0) {
                this.resetFlyingSpeed();
            }
            this.setRestTick(Math.max(this.getRestTick() - 1, 0));
            if (this.isAngry() && this.getRestTick() == 0) {
                this.calmDown();
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new PunishingBirdBodyRotationControl(this);
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, NORMAL_MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, NORMAL_ATTACK_DAMAGE)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.FLYING_SPEED, 0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PunishingBirdAngryAttackGoal(this, 2.0F, true));
        this.goalSelector.addGoal(3, new PunishingBirdNormalAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, true, true, (livingEntity) ->
                        (livingEntity instanceof Mob) && !(livingEntity instanceof PunishingBirdEntity) || livingEntity instanceof Player) {

            @Override
            protected AABB getTargetSearchArea(double pTargetDistance) {
                return this.mob.getBoundingBox().inflate(pTargetDistance);//デフォルトから上下方向に感知範囲を広げた
            }
        });
    }

    private <P extends Entity & IAnimatable> PlayState bodyPredicate(AnimationEvent<P> event) {
        if (!this.isOnGround()) {
            event.getController().setAnimation(ANIM_FLY);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    private <P extends Entity & IAnimatable> PlayState attackPredicate(AnimationEvent<P> event) {
        if (this.isAngry() && this.swinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ANIM_ATTACK_ANGRY);
        } else if (this.swinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ANIM_ATTACK_NORMAL);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller",
                0, this::bodyPredicate));
        data.addAnimationController(new AnimationController<>(this, "attack_controller",
                0, this::attackPredicate));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_ANGRY, false);
        this.getEntityData().define(ATTACK_TICK, WAIT_ANIMATION_TICK);
    }

    public boolean isAngry() {
        return this.getEntityData().get(IS_ANGRY);
    }

    public void setAngry(boolean flag) {
        this.getEntityData().set(IS_ANGRY, flag);
    }

    public int getAttackTick() {
        return this.getEntityData().get(ATTACK_TICK);
    }

    private void setAttackTick(int tick) {
        this.getEntityData().set(ATTACK_TICK, tick);
    }

    public void startAttackAnim() {
        this.getEntityData().set(ATTACK_TICK, WAIT_ANIMATION_TICK);
    }

    public int getRestTick() {
        return restTick;
    }

    public void setRestTick(int tick) {
        this.restTick = tick;
    }

    public void resetRestTick() {
        this.restTick = ENOUGH_REST_TICK;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("angry", isAngry());
        pCompound.putInt("rest_tick", getRestTick());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setAngry(pCompound.getBoolean("angry"));
        setRestTick(pCompound.getInt("rest_tick"));
    }

    public AABB getAngryAttackAABB(float pPartialTicks) {
        return this.getBoundingBox().move(this.getViewVector(pPartialTicks).multiply(1.0F,0.0F,1.0F).normalize().scale(2.25F)).inflate(1.25F);
    }


    private static class PunishingBirdBodyRotationControl extends BodyRotationControl {

        private final PunishingBirdEntity owner;

        public PunishingBirdBodyRotationControl(PunishingBirdEntity punishingBird) {
            super(punishingBird);
            this.owner = punishingBird;
        }

        @Override
        public void clientTick() {
            if (this.owner.getAttackTick() > 0) {
                this.owner.yBodyRot = this.owner.yHeadRot;
                return;
            }
            super.clientTick();
        }
    }

    private static class PunishingBirdLookControl extends LookControl {

        private final PunishingBirdEntity owner;

        public PunishingBirdLookControl(PunishingBirdEntity punishingBird) {
            super(punishingBird);
            this.owner = punishingBird;
        }

        @Override
        public void tick() {
            if (this.owner.getAttackTick() > 0) {
                return;
            }
            super.tick();
        }
    }

    /**
     * 通常攻撃
     */
     private static class PunishingBirdNormalAttackGoal extends MeleeAttackGoal {
        protected PunishingBirdEntity owner;

        public PunishingBirdNormalAttackGoal(PunishingBirdEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.owner = pMob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.owner.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.owner.isAngry();
        }
    }

    /**
     * 特殊攻撃
     */
    private static class PunishingBirdAngryAttackGoal extends MeleeAttackGoal {
        protected final PunishingBirdEntity owner;
        protected boolean isPunishing;

        public PunishingBirdAngryAttackGoal(PunishingBirdEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.owner = pMob;
        }

        @Override
        public void start() {
            super.start();
            this.isPunishing = false;
        }

        @Override
        public void stop() {
            super.stop();
            this.owner.resetFlyingSpeed();
            this.isPunishing = false;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.owner.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.owner.isAngry();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            AABB attackRange = this.owner.getAngryAttackAABB(1.0F);
            List<LivingEntity> listInAttackRange = this.owner.level.getEntitiesOfClass(LivingEntity.class, attackRange);
            List<LivingEntity> listInReadyRange = this.owner.level.getEntitiesOfClass(LivingEntity.class, attackRange.inflate(-0.5F));

            if (this.isPunishing && this.owner.getAttackTick() == 5 && listInAttackRange.contains(pEnemy)) {
                for (LivingEntity livingEntity : listInAttackRange) {
                    if (livingEntity != this.owner) this.owner.doHurtTarget(livingEntity);
                }
                this.owner.resetFlyingSpeed();
            }

            if (!this.isPunishing && listInReadyRange.contains(pEnemy)) {
                this.owner.swing(InteractionHand.MAIN_HAND);
                this.owner.startAttackAnim();
                this.owner.stopFlyingSpeed();
                this.isPunishing = true;
            }

            if (this.owner.getAttackTick() == 0) {
                this.isPunishing = false;
            }
        }
    }
}

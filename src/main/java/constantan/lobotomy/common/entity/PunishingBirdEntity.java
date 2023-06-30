package constantan.lobotomy.common.entity;

import constantan.lobotomy.lib.LibDebug;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
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
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PunishingBirdEntity extends Monster implements IAnimatable {

    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(PunishingBirdEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(PunishingBirdEntity.class, EntityDataSerializers.INT);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected static final AnimationBuilder IDLE_NORMAL = new AnimationBuilder()
            .addAnimation("animation.punishing_bird.idle_normal", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder FLY_NORMAL = new AnimationBuilder()
            .addAnimation("animation.punishing_bird.fly_normal", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ATTACK_NORMAL = new AnimationBuilder()
            .addAnimation("animation.punishing_bird.attack_normal", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder FLY_ANGRY = new AnimationBuilder()
            .addAnimation("animation.punishing_bird.fly_angry", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ATTACK_ANGRY = new AnimationBuilder()
            .addAnimation("animation.punishing_bird.attack_angry", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public static final double NORMAL_MAX_HEALTH = 200.0D;
    public static final double ANGRY_MAX_HEALTH = 1000.0D;

    public static final double NORMAL_ATTACK_DAMAGE = 1.0D;
    public static final double ANGRY_ATTACK_DAMAGE = 1000.0D;

    public PunishingBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == DamageSource.FALL) return false;
        boolean flag = super.hurt(pSource, pAmount);
        if (flag && this.getMaxHealth() < ANGRY_MAX_HEALTH && pSource != DamageSource.OUT_OF_WORLD) {
            double maxHealthModifier = ANGRY_MAX_HEALTH - this.getMaxHealth();
            this.getAttribute(Attributes.MAX_HEALTH)
                    .addTransientModifier(new AttributeModifier("MaxHealth", maxHealthModifier, AttributeModifier.Operation.ADDITION));
            this.setHealth(this.getMaxHealth() - pAmount);
            this.setAngry(true);
        }
        return flag;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, NORMAL_MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, NORMAL_ATTACK_DAMAGE)
                .add(Attributes.ATTACK_SPEED, 1.0D)
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
                return this.mob.getBoundingBox().inflate(pTargetDistance);
            }

        });
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) this.getEntityData().set(ATTACK_TICK, Math.max(this.getAttackTick() - 1, 0));
    }

    private <E extends PunishingBirdEntity>PlayState bodyPredicate(final AnimationEvent<E> event) {
        if (this.isAngry()) {
            event.getController().setAnimation(FLY_ANGRY);
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(FLY_NORMAL);
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(IDLE_NORMAL);
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {
        if (this.isAngry() && this.swinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ATTACK_ANGRY);
        } else if (this.swinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ATTACK_NORMAL);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController(this, "body_controller",
                0, this::bodyPredicate));
        data.addAnimationController(new AnimationController(this, "attack_controller",
                0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_ANGRY, false);
        this.getEntityData().define(ATTACK_TICK, 50);
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

    public void startAttackAnim() {
    this.getEntityData().set(ATTACK_TICK, 50);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("angry", isAngry());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setAngry(pCompound.getBoolean("angry"));
    }

    @Override
    protected Component getTypeName() {//表示名変えるやつ
        String descriptionID = super.getType().getDescriptionId();
        return isAngry() ? new TranslatableComponent(descriptionID) : new TranslatableComponent(descriptionID + ".unknown");
    }

    /**
     * 通常攻撃
     */
    static class PunishingBirdNormalAttackGoal extends MeleeAttackGoal {
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
    public class PunishingBirdAngryAttackGoal extends MeleeAttackGoal {
        protected PunishingBirdEntity owner;
        protected boolean isPunishing = false;

        public PunishingBirdAngryAttackGoal(PunishingBirdEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.owner = pMob;
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
            double d0 = this.getAttackReachSqr(pEnemy);
            boolean isValidRange = pDistToEnemySqr <= d0;
            boolean isReadyRange = pDistToEnemySqr <= d0 * 0.75D;
            if (this.isPunishing && this.owner.getAttackTick() == 25) {
                if (isValidRange) this.mob.doHurtTarget(pEnemy);
                LibDebug log = new LibDebug();
                log.addChatMessage(isValidRange ? "attack!" : "miss!");
            }
            if (!this.isPunishing && isReadyRange) {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.owner.startAttackAnim();
                this.owner.getAttribute(Attributes.FLYING_SPEED)
                        .addTransientModifier(new AttributeModifier("FlyingSpeed", 0.0F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                double attackDamageModifier = ANGRY_ATTACK_DAMAGE - this.owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                this.owner.getAttribute(Attributes.ATTACK_DAMAGE)
                        .addTransientModifier(new AttributeModifier("AttackDamage", attackDamageModifier, AttributeModifier.Operation.ADDITION));
                this.isPunishing = true;
            }

            if (this.owner.getAttackTick() == 0) {
                this.owner.getAttribute(Attributes.FLYING_SPEED).removeModifiers();
                this.isPunishing = false;
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return super.getAttackReachSqr(pAttackTarget) + 20.0D;
        }
    }
}

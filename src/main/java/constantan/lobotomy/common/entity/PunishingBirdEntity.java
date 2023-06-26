package constantan.lobotomy.common.entity;

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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

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

    public static final float NORMAL_MAX_HEALTH = 200.0f;
    public static final float ANGRY_MAX_HEALTH = 1000.0f;

    private boolean angry = false;

    public PunishingBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean flag = super.hurt(pSource, pAmount);
        if (flag && this.getMaxHealth() < ANGRY_MAX_HEALTH) {
            float maxHealthModifier = ANGRY_MAX_HEALTH - this.getMaxHealth();
            this.getAttribute(Attributes.MAX_HEALTH)
                    .addTransientModifier(new AttributeModifier("MaxHealth", maxHealthModifier, AttributeModifier.Operation.ADDITION));
            this.setHealth(this.getMaxHealth());
            this.setAngry(true);
        }
        return flag;
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, NORMAL_MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, 10.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FLYING_SPEED, 0.4f).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PunishingBirdAngryAttackGoal(this, 2.0f, false));
        this.goalSelector.addGoal(3, new PunishingBirdNormalAttackGoal(this, 1.0f, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, false, false, (livingEntity) ->
                        (livingEntity instanceof Mob) && !(livingEntity instanceof PunishingBirdEntity)));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);
    }

    private <E extends PunishingBirdEntity>PlayState predicate(final AnimationEvent<E> event) {
        if (this.isAngry()) {
            if (this.getAttackTick() > 0) {
                event.getController().markNeedsReload();
                event.getController().setAnimation(ATTACK_ANGRY);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(FLY_ANGRY);
            return PlayState.CONTINUE;
        } else if (this.swinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ATTACK_NORMAL);
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimation(FLY_NORMAL);
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(IDLE_NORMAL);
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {
//        if(event.getController().getAnimationState().equals(AnimationState.Stopped)) {
//            event.getController().markNeedsReload();
//            if (this.getAttackTick() > 0) {
//                event.getController().setAnimation(ATTACK_ANGRY);
//            } else if (this.swinging) {
//                event.getController().setAnimation(ATTACK_NORMAL);
//            }
//            this.swinging = false;
//        }
//
//        return PlayState.CONTINUE;
        if (this.getAttackTick() > 0) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(ATTACK_ANGRY);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_ANGRY, false);
        this.getEntityData().define(ATTACK_TICK, 30);
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
    this.getEntityData().set(ATTACK_TICK, 30);
    }

    @Override
    protected Component getTypeName() {//表示名変えるやつ
        String descriptionID = super.getType().getDescriptionId();
        return isAngry() ? new TranslatableComponent(descriptionID) : new TranslatableComponent(descriptionID + ".unknown");
    }

    static class PunishingBirdNormalAttackGoal extends MeleeAttackGoal {
        PunishingBirdEntity owner;

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

    public class PunishingBirdAngryAttackGoal extends Goal{//MeleeAttackGoalのコピペを少し改変
        protected final PunishingBirdEntity mob;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public PunishingBirdAngryAttackGoal(PunishingBirdEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            this.mob = pMob;
            this.speedModifier = pSpeedModifier;
            this.followingTargetEvenIfNotSeen = pFollowingTargetEvenIfNotSeen;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            if (!this.mob.isAngry()) {
                return false;
            }
            long i = this.mob.level.getGameTime();
            if (i - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = i;
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else {
                    if (canPenalize) {
                        if (--this.ticksUntilNextPathRecalculation <= 0) {
                            this.path = this.mob.getNavigation().createPath(livingentity, 0);
                            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                            return this.path != null;
                        } else {
                            return true;
                        }
                    }
                    this.path = this.mob.getNavigation().createPath(livingentity, 0);
                    if (this.path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            if (!this.mob.isAngry()) {
                return false;
            }
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget((LivingEntity)null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                        if (this.mob.getNavigation().getPath() != null) {
                            net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                            if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                                failedPathFindingPenalty = 0;
                            else
                                failedPathFindingPenalty += 10;
                        } else {
                            failedPathFindingPenalty += 10;
                        }
                    }
                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
                if (this.mob.getAttackTick() == 0) {
                    this.mob.startAttackAnim();
                }
                if (this.mob.getAttackTick() < 5) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(pEnemy);
                }
            }

        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(60);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(60);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth()) + 32.0D;
        }
    }
}

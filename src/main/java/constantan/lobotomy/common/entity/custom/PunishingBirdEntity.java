package constantan.lobotomy.common.entity.custom;

import constantan.lobotomy.common.entity.*;
import constantan.lobotomy.common.entity.ai.behaviour.AnimatableRangedAoEAttack;
import constantan.lobotomy.common.entity.ai.behaviour.FloatToSurfaceOfFluidWithSafety;
import constantan.lobotomy.common.entity.ai.behaviour.SetPlayerTransientLookTarget;
import constantan.lobotomy.common.entity.ai.behaviour.SetVariableSpeedWalkTargetToAttackTarget;
import constantan.lobotomy.common.entity.ai.control.LazyFlyingMoveControl;
import constantan.lobotomy.common.entity.ai.sensor.LivingEntityInAoESensor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class PunishingBirdEntity extends SmartBrainAbnormalityEntity<PunishingBirdEntity>
        implements IAnimatable, IQliphoth, IAoEAttackMob, ILazyControlMob<PunishingBirdEntity> {

    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData
            .defineId(PunishingBirdEntity.class, EntityDataSerializers.BOOLEAN);

    private static final AnimationBuilder ANIM_ATTACK_NORMAL = new AnimationBuilder()
            .addAnimation("attack_normal", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private static final AnimationBuilder ANIM_ATTACK_ANGRY = new AnimationBuilder()
            .addAnimation("attack_angry", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private static final UUID MAX_HEALTH_MODIFIER_UUID = UUID.fromString("7354db20-6023-4192-a616-086dc218bc96");
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("e7c57c66-49f4-4a35-9f37-1be7b2e06952");

    private static final String ATTRIBUTE_MODIFIER_NAME = "Punishing Bird Modifier";

    private static final float NORMAL_MAX_HEALTH = 200.0F;
    private static final float ANGRY_MAX_HEALTH = 1000.0F;

    private static final double NORMAL_ATTACK_DAMAGE = 1.0D;
    private static final double ANGRY_ATTACK_DAMAGE = 1000.0D;

    private static final int WAIT_ANIM_TICK = 30;
    private static final int ATTACK_OCCUR_TICK = 21;

    private static final int ENOUGH_REST_TICK = 20 * 60;

    private int restTick = 0;

    public PunishingBirdEntity(EntityType<PunishingBirdEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public List<ExtendedSensor<PunishingBirdEntity>> getSensors() {
        return this.sensors(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>(),
                new LivingEntityInAoESensor<PunishingBirdEntity>()
                        .attackRange(PunishingBirdEntity::getAngryAttackAABB)
                        .checkRange(punishingBird -> punishingBird.getAngryAttackAABB().inflate(-0.5F))
                        .setScanRate(punishingBird -> punishingBird.isAngry() ? 5 : 20)
        );
    }

    @Override
    public BrainActivityGroup<PunishingBirdEntity> getCoreTasks() {
        return this.coreTasks(
                new FloatToSurfaceOfFluidWithSafety<>(),
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<PunishingBirdEntity> getIdleTasks() {
        return this.idleTasks(
                this.firstApplicableBehaviour(
                        new TargetOrRetaliate<>(),
                        new SetPlayerTransientLookTarget<>()
                                .predicate(player -> player.distanceTo(this) < 4),
                        new SetRandomLookTarget<>()
                ), this.oneRandomBehaviour(
                        new SetRandomWalkTarget<>(),
                        new Idle<PunishingBirdEntity>()
                                .runFor(punishingBird -> punishingBird.getRandom().nextInt(200, 400))
                )
        );
    }

    @Override
    public BrainActivityGroup<PunishingBirdEntity> getFightTasks() {
        return this.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetVariableSpeedWalkTargetToAttackTarget<PunishingBirdEntity>()
                        .speedMod((punishingBird, livingEntity) -> punishingBird.isAngry() ? 2.0F : 1.0F),
                new AnimatableRangedAoEAttack<PunishingBirdEntity>(ATTACK_OCCUR_TICK + 1)
                        .startCondition(punishingBird -> !this.isAttackAnimating.test(punishingBird) && punishingBird.isAngry())
                        .whenStarting(punishingBird -> punishingBird.setAttackTick(WAIT_ANIM_TICK + 1))
                        .stopIf(punishingBird -> !punishingBird.isAngry()),
                new AnimatableMeleeAttack<PunishingBirdEntity>(2)
                        .startCondition(punishingBird -> !this.isAttackAnimating.test(punishingBird) && !punishingBird.isAngry())
                        .whenStarting(punishingBird -> punishingBird.setAttackTick(2))
                        .stopIf(PunishingBirdEntity::isAngry)
        );
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller", 0, event -> {
            if (!this.isOnGround()) {
                event.getController().setAnimation(ANIM_FLY);
            } else {
                event.getController().setAnimation(ANIM_IDLE);
            }
            return PlayState.CONTINUE;
        }));
        data.addAnimationController(new AnimationController<>(this, "attack_controller", 0, event -> {
            if (this.isAngry()) {
                if (this.getAttackTick() == WAIT_ANIM_TICK) {
                    event.getController().markNeedsReload();
                    event.getController().setAnimation(ANIM_ATTACK_ANGRY);
                }
            } else if (this.getAttackTick() == 1) {
                event.getController().markNeedsReload();
                event.getController().setAnimation(ANIM_ATTACK_NORMAL);
            }
            return PlayState.CONTINUE;
        }));
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, NORMAL_MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, NORMAL_ATTACK_DAMAGE)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.FLYING_SPEED, 0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .build();
    }

    public AABB getAngryAttackAABB() {
        return this.getAngryAttackAABB(1.0F);
    }

    public AABB getAngryAttackAABB(float partialTicks) {
        return this.getBoundingBox()
                .move(this.getViewVector(partialTicks)
                        .multiply(1.0F,0.0F,1.0F)
                        .normalize().scale(2.25F))
                .inflate(1.25F);
    }

    public void anger() {
        this.getAttribute(Attributes.ATTACK_DAMAGE)
                .addPermanentModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, ATTRIBUTE_MODIFIER_NAME,
                        ANGRY_ATTACK_DAMAGE - NORMAL_ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION));

        float healthRatio = this.getHealth() / this.getMaxHealth();
        this.getAttribute(Attributes.MAX_HEALTH)
                .addPermanentModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_UUID, ATTRIBUTE_MODIFIER_NAME,
                        ANGRY_MAX_HEALTH - NORMAL_MAX_HEALTH, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth() * healthRatio);

        this.setAngry(true);
    }

    public void calmDown() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).removePermanentModifier(ATTACK_DAMAGE_MODIFIER_UUID);

        float healthRatio = this.getHealth() / this.getMaxHealth();
        this.getAttribute(Attributes.MAX_HEALTH).removePermanentModifier(MAX_HEALTH_MODIFIER_UUID);
        this.setHealth(this.getMaxHealth() * healthRatio);

        this.setAngry(false);
    }

    public boolean isAngry() {
        return this.getEntityData().get(IS_ANGRY);
    }

    public void setAngry(boolean flag) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(IS_ANGRY, flag);
        }
    }

    public int getRestTick() {
        return restTick;
    }

    public void setRestTick(int tick) {
        if (!this.level.isClientSide) {
            this.restTick = tick;
        }
    }

    public void resetRestTick() {
        if (!this.level.isClientSide) {
            this.restTick = ENOUGH_REST_TICK;
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
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
        this.setRestTick(Math.max(this.getRestTick() - 1, 0));
        if (!this.level.isClientSide) {
            if (this.isAngry() && this.getRestTick() == 0 && !this.isAttackAnimating.test(this)) {
                this.calmDown();
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_ANGRY, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("angry", isAngry());
        pCompound.putInt("rest_tick", getRestTick());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setAngry(pCompound.getBoolean("angry"));
        setRestTick(pCompound.getInt("rest_tick"));
    }

    @Override
    public Lazy<Predicate<PunishingBirdEntity>> getPredicateLazy() {
        return Lazy.of(() -> this.isAttackAnimating.and(PunishingBirdEntity::isAngry));
    }

    @Override
    public MoveControl createLazyMoveControl() {
        return new LazyFlyingMoveControl<>(this, 20, false)
                .isLazyIf(this.getPredicateLazy());
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level pLevel) {
        return new FlyingPathNavigation(this, pLevel);
    }
}

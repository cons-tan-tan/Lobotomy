package constantan.lobotomy.common.entity.custom;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.entity.IAoEAttackMob;
import constantan.lobotomy.common.entity.ILazyControlMob;
import constantan.lobotomy.common.entity.SmartBrainAbnormalityEntity;
import constantan.lobotomy.common.entity.ai.behaviour.SetPlayerTransientLookTarget;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;
import java.util.function.Predicate;

public class JudgementBirdEntity extends SmartBrainAbnormalityEntity<JudgementBirdEntity>
        implements IAnimatable, IAoEAttackMob, ILazyControlMob<JudgementBirdEntity> {

    private static final EntityDataAccessor<Boolean> IS_SPONTANEOUSLY_MOVING = SynchedEntityData
            .defineId(JudgementBirdEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int ATTACK_DAMAGE_RANDOM_RANGE = 10;

    private static final int WAIT_ANIM_TICK = 85;
    private static final int ATTACK_OCCUR_TICK = 75;

    public JudgementBirdEntity(EntityType<JudgementBirdEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller", 0, event -> {
            boolean isMoving = this.getEntityData().get(IS_SPONTANEOUSLY_MOVING);
            if (this.getAttackTick() > 0) {
                return PlayState.STOP;
            } else if (this.isOnGround() && isMoving) {
                event.getController().setAnimation(ANIM_WALK);
            } else {
                event.getController().setAnimation(ANIM_IDLE);
            }
            return PlayState.CONTINUE;
        }));
        data.addAnimationController(new AnimationController<>(this, "attack_controller", 0, event -> {
            if (this.getAttackTick() == WAIT_ANIM_TICK) {
                event.getController().markNeedsReload();
                event.getController().setAnimation(ANIM_ATTACK);
                return PlayState.CONTINUE;
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public List<ExtendedSensor<JudgementBirdEntity>> getSensors() {
        return this.sensors(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<JudgementBirdEntity> getCoreTasks() {
        return this.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget<JudgementBirdEntity>()
                        .startCondition(judgementBird -> judgementBird.getAttackTick() == 0)
                        .stopIf(judgementBird -> judgementBird.getAttackTick() > 0),
                new MoveToWalkTarget<JudgementBirdEntity>()
                        .startCondition(judgementBird -> judgementBird.getAttackTick() == 0)
        );
    }

    @Override
    public BrainActivityGroup<JudgementBirdEntity> getIdleTasks() {
        return this.idleTasks(
                this.firstApplicableBehaviour(
                        new TargetOrRetaliate<>(),
                        new SetPlayerTransientLookTarget<>()
                                .predicate(player -> player.distanceTo(this) < 4),
                        new SetRandomLookTarget<>()
                ), this.oneRandomBehaviour(
                        new SetRandomWalkTarget<>(),
                        new Idle<JudgementBirdEntity>()
                                .runFor(judgementBird -> judgementBird.getRandom().nextInt(200, 400))
                ));
    }

    @Override
    public BrainActivityGroup<JudgementBirdEntity> getFightTasks() {
        return this.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<JudgementBirdEntity>(ATTACK_OCCUR_TICK + 1)
                        .startCondition(judgementBird -> judgementBird.getAttackTick() == 0)
                        .whenStarting(judgementBird -> judgementBird.setAttackTick(WAIT_ANIM_TICK + 1))
        );
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .build();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.getEntityData().set(IS_SPONTANEOUSLY_MOVING, this.zza != 0);
        }
    }

    @Override
    public double getAttributeValue(@NotNull Attribute pAttribute) {
        if (pAttribute == Attributes.ATTACK_DAMAGE) {
            return super.getAttributeValue(pAttribute) + this.getRandom().nextInt(ATTACK_DAMAGE_RANDOM_RANGE + 1);
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_SPONTANEOUSLY_MOVING, false);
    }

    @Override
    public Lazy<Predicate<JudgementBirdEntity>> getPredicateLazy() {
        return Lazy.of(() -> this.isAttackAnimating);
    }
}

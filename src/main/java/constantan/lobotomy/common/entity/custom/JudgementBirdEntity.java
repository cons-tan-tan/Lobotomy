package constantan.lobotomy.common.entity.custom;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.entity.SmartBrainAbnormalityEntity;
import constantan.lobotomy.common.entity.ai.behaviour.SetPlayerTransientLookTarget;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
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

public class JudgementBirdEntity extends SmartBrainAbnormalityEntity<JudgementBirdEntity> implements IAnimatable {

    private static final int ATTACK_DAMAGE_RANDOM_RANGE = 10;

    private static final int WAIT_ANIM_TICK = 85;
    private static final int ATTACK_OCCUR_TICK = 75;

    public JudgementBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller", 0, event -> {
            if (this.getAttackTick() > 0) {
                return PlayState.STOP;
            } else if (this.isOnGround() && event.isMoving()) {
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
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<JudgementBirdEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
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
        return BrainActivityGroup.<JudgementBirdEntity>idleTasks(//ここのジェネリクスを省略するとコンパイル通らない
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>(),
                        new SetPlayerTransientLookTarget<>()
                                .predicate(player -> player.distanceTo(this) < 4),
                        new SetRandomLookTarget<>()
                ), new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>()
                                .runFor(livingEntity -> livingEntity.getRandom().nextInt(200, 400))
                ));
    }

    @Override
    public BrainActivityGroup<JudgementBirdEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<JudgementBirdEntity>(ATTACK_OCCUR_TICK + 1)
                        .startCondition(judgementBird -> judgementBird.getAttackTick() == 0)
        );
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F).build();
    }

    @Override
    public void swing(@NotNull InteractionHand pHand, boolean pUpdateSelf) {
        super.swing(pHand, pUpdateSelf);
        this.setAttackTick(WAIT_ANIM_TICK + 1);
    }

    @Override
    public double getAttributeValue(@NotNull Attribute pAttribute) {
        if (pAttribute == Attributes.ATTACK_DAMAGE) {
            return super.getAttributeValue(pAttribute) + this.getRandom().nextInt(ATTACK_DAMAGE_RANDOM_RANGE + 1);
        }
        return super.getAttributeValue(pAttribute);
    }

    @Override
    public @NotNull Predicate<JudgementBirdEntity> canDoUnblockableAttack() {
        return judgementBirdEntity -> true;
    }

    @Override
    public boolean canDoKnockbackAttack() {
        return false;
    }
}

package constantan.lobotomy.common.entity.custom;

import constantan.lobotomy.common.entity.*;
import constantan.lobotomy.common.entity.ai.behaviour.FloatToSurfaceOfFluidWithSafety;
import constantan.lobotomy.common.entity.ai.behaviour.SetPlayerTransientLookTarget;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;
import java.util.function.Predicate;

public class BigBird extends SmartBrainAbnormalityEntity<BigBird>
        implements IAnimatable, IQliphoth, IAoEAttack, ISyncSpontaneousMoving,
        ITransientNoCulling<BigBird>, ILazyControl<BigBird> {

    public BigBird(EntityType<BigBird> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public List<ExtendedSensor<BigBird>> getSensors() {
        return this.sensors(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<BigBird> getCoreTasks() {
        return this.coreTasks(
                new FloatToSurfaceOfFluidWithSafety<>(),
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<BigBird> getIdleTasks() {
        return this.idleTasks(
                this.firstApplicableBehaviour(
                        new TargetOrRetaliate<>(),
                        new SetPlayerTransientLookTarget<>()
                                .predicate(player -> player.distanceTo(this) < 4),
                        new SetRandomLookTarget<>()
                ), this.oneRandomBehaviour(
                        new SetRandomWalkTarget<>(),
                        new Idle<BigBird>()
                                .runFor(bigBird -> bigBird.getRandom().nextInt(200, 400))
                )
        );
    }

    @Override
    public BrainActivityGroup<BigBird> getFightTasks() {
        return this.fightTasks(
                new InvalidateAttackTarget<>(),
                new MeleeAttack(20)
        );
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1600)
                .add(Attributes.ATTACK_DAMAGE, 2048)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .build();
    }

    @Override
    public Lazy<Predicate<BigBird>> getPredicateLazy() {
        return Lazy.of(() -> isAttackAnimating);
    }
}

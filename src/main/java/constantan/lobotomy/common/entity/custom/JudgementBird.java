package constantan.lobotomy.common.entity.custom;

import constantan.lobotomy.common.entity.*;
import constantan.lobotomy.common.entity.ai.behaviour.AnimatableRangedAoEAttack;
import constantan.lobotomy.common.entity.ai.behaviour.FloatToSurfaceOfFluidWithSafety;
import constantan.lobotomy.common.entity.ai.behaviour.SetCustomSpeedWalkTargetToAttackTargetWithAoE;
import constantan.lobotomy.common.entity.ai.behaviour.SetPlayerTransientLookTarget;
import constantan.lobotomy.common.entity.ai.sensor.LivingEntityInAoESensor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.entity.PartEntity;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;
import java.util.function.Predicate;

public class JudgementBird extends SmartBrainAbnormalityEntity<JudgementBird>
        implements IAnimatable, IQliphoth, IAoEAttack, ISyncSpontaneousMoving, IMultiPart,
        ITransientNoCulling<JudgementBird>, ILazyControl<JudgementBird> {

    private static final int ATTACK_DAMAGE_RANDOM_RANGE = 10;

    private static final int WAIT_ANIM_TICK = 85;
    private static final int ATTACK_OCCUR_TICK = 75;

    private final CommonPartEntity<?>[] subEntities;
    private final CommonPartEntity<?> head;
    private final CommonPartEntity<?> body;

    public JudgementBird(EntityType<JudgementBird> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.head = new CommonPartEntity<>(this, "head", 0.9F, 1.1F);
        this.body = new CommonPartEntity<>(this, "body", 0.9F, 2.1F);
        this.subEntities = new CommonPartEntity[]{this.head, this.body};

        this.multiPartInit();
    }

    @Override
    public PartEntity<?>[] getSubParts() {
        return this.subEntities;
    }

    @Override
    public void tick() {
        super.tick();

        var vec3 = new Vec3[this.subEntities.length];
        for(int j = 0; j < this.subEntities.length; ++j) {
            vec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
        }

        float rad = this.yBodyRot * ((float)Math.PI / 180F);
        float sin = Mth.sin(rad);
        float cos = Mth.cos(rad);
        this.tickPart(this.head, -sin * 0.7F, 2.1F, cos * 0.7F);
        this.tickPart(this.body, 0.0F, 0.0F, 0.0F);

        for(int l = 0; l < this.subEntities.length; ++l) {
            this.subEntities[l].xo = vec3[l].x;
            this.subEntities[l].yo = vec3[l].y;
            this.subEntities[l].zo = vec3[l].z;
            this.subEntities[l].xOld = vec3[l].x;
            this.subEntities[l].yOld = vec3[l].y;
            this.subEntities[l].zOld = vec3[l].z;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller", 0, event -> {
            var controller = event.getController();
            if (this.getAttackTick() > 0) {
                return PlayState.STOP;
            } else if (this.isOnGround() && this.isMovingSpontaneously()) {
                controller.setAnimation(ANIM_WALK);
            } else {
                controller.setAnimation(ANIM_IDLE);
            }
            return PlayState.CONTINUE;
        }));
        data.addAnimationController(new AnimationController<>(this, "attack_controller", 0, event -> {
            var controller = event.getController();
            if (this.getAttackTick() > 0) {
                controller.setAnimation(ANIM_ATTACK);
                return PlayState.CONTINUE;
            } else {
                controller.clearAnimationCache();
            }
            return PlayState.STOP;
        }));
    }

    @Override
    public List<ExtendedSensor<JudgementBird>> getSensors() {
        return this.sensors(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>(),
                new LivingEntityInAoESensor<JudgementBird>()
                        .attackRange(judgementBird -> getBoundingBox().inflate(16, 1, 16))
                        .checkRange(judgementBird -> getBoundingBox().inflate(12, 0, 12))
        );
    }

    @Override
    public BrainActivityGroup<JudgementBird> getCoreTasks() {
        return this.coreTasks(
                new FloatToSurfaceOfFluidWithSafety<>(),
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<JudgementBird> getIdleTasks() {
        return this.idleTasks(
                this.firstApplicableBehaviour(
                        new TargetOrRetaliate<>(),
                        new SetPlayerTransientLookTarget<>()
                                .predicate(player -> player.distanceTo(this) < 4),
                        new SetRandomLookTarget<>()
                ), this.oneRandomBehaviour(
                        new SetRandomWalkTarget<>(),
                        new Idle<JudgementBird>()
                                .runFor(judgementBird -> judgementBird.getRandom().nextInt(200, 400))
                ));
    }

    @Override
    public BrainActivityGroup<JudgementBird> getFightTasks() {
        return this.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetCustomSpeedWalkTargetToAttackTargetWithAoE<>(),
                new AnimatableRangedAoEAttack<JudgementBird>(ATTACK_OCCUR_TICK + 1)
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
    public double getAttributeValue(@NotNull Attribute pAttribute) {
        if (pAttribute == Attributes.ATTACK_DAMAGE) {
            return super.getAttributeValue(pAttribute) + this.getRandom().nextInt(ATTACK_DAMAGE_RANDOM_RANGE + 1);
        }
        return super.getAttributeValue(pAttribute);
    }

    @Override
    public Lazy<Predicate<JudgementBird>> getPredicateLazy() {
        return Lazy.of(() -> this.isAttackAnimating);
    }
}

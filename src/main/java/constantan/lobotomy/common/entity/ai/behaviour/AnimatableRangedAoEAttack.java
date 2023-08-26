package constantan.lobotomy.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import constantan.lobotomy.common.entity.IAoEAttackMob;
import constantan.lobotomy.common.init.ModMemoryModuleTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

public class AnimatableRangedAoEAttack<E extends Mob> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryModuleTypes.IN_AOE_LIVING_ENTITY.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)
    );

    protected BiConsumer<E, List<LivingEntity>> doAoEAttackConsumer = (owner, list) -> {
        if (owner instanceof IAoEAttackMob iAoEAttackMob) iAoEAttackMob.performAoEAttack(list);
    };
    protected ToIntFunction<E> attackIntervalFunction = entity -> 20;
    protected BiPredicate<E, LivingEntity> exceptSelf = (self, target) -> true;
    protected BiPredicate<E, LivingEntity> exceptTargetIf = (owner, target) -> false;

    @Nullable
    protected LivingEntity target = null;

    public AnimatableRangedAoEAttack(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public AnimatableRangedAoEAttack<E> rangedAoEAttack(BiConsumer<E, List<LivingEntity>> doAoEAttackConsumer) {
        this.doAoEAttackConsumer = doAoEAttackConsumer;
        return this;
    }

    public AnimatableRangedAoEAttack<E> attackInterval(ToIntFunction<E> attackIntervalFunction) {
        this.attackIntervalFunction = attackIntervalFunction;
        return this;
    }

    public AnimatableRangedAoEAttack<E> exceptSelf(BiPredicate<E, LivingEntity> exceptSelfPredicate) {
        this.exceptSelf = exceptSelfPredicate;
        return this;
    }

    public AnimatableRangedAoEAttack<E> exceptTargetIf(BiPredicate<E, LivingEntity> exceptTargetPredicate) {
        this.exceptTargetIf = exceptTargetPredicate;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);

        return entity.getSensing().hasLineOfSight(this.target) && this.isWithinAoEAttackRange(entity, this.target);
    }

    public boolean isWithinAoEAttackRange(E entity, LivingEntity livingEntity) {
        List<LivingEntity> checkList = BrainUtils.getMemory(entity, ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get());
        return checkList != null && checkList.contains(livingEntity);
    }

    @Override
    protected void start(E entity) {
        entity.swing(InteractionHand.MAIN_HAND);
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    @Override
    protected void doDelayedAction(E owner) {
        BrainUtils.setForgettableMemory(owner, MemoryModuleType.ATTACK_COOLING_DOWN, true,
                this.attackIntervalFunction.applyAsInt(owner));

        if (this.target == null)
            return;

        if (!owner.getSensing().hasLineOfSight(this.target) || !this.isWithinAoEAttackRange(owner, this.target))
            return;

        List<LivingEntity> livingEntities = BrainUtils.getMemory(owner, ModMemoryModuleTypes.IN_AOE_LIVING_ENTITY.get());
        if (this.exceptSelf.test(owner, target)) {
            livingEntities.remove(owner);
        }
        livingEntities.removeIf(livingEntity -> this.exceptTargetIf.test(owner, livingEntity));

        this.doAoEAttackConsumer.accept(owner, livingEntities);
    }
}

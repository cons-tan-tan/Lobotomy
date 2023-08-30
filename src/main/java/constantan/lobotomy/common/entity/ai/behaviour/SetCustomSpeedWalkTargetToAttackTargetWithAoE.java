package constantan.lobotomy.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import constantan.lobotomy.common.init.ModMemoryModuleTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class SetCustomSpeedWalkTargetToAttackTargetWithAoE<E extends Mob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get(), MemoryStatus.REGISTERED)
    );

    protected BiFunction<E, LivingEntity, Float> speedMod = ((owner, livingEntity) -> 1.0F);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public SetCustomSpeedWalkTargetToAttackTargetWithAoE<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    private boolean isWithinAoEAttackRange(E entity, LivingEntity livingEntity) {
        List<LivingEntity> checkList = BrainUtils.getMemory(entity, ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get());
        return livingEntity != null && checkList != null && checkList.contains(livingEntity);
    }

    @Override
    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if (entity.getSensing().hasLineOfSight(target) && this.isWithinAoEAttackRange(entity, target)) {
            BrainUtils.clearMemory(brain, MemoryModuleType.WALK_TARGET);
        }
        else {
            BrainUtils.setMemory(brain, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
            BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false),
                    this.speedMod.apply(entity, target), 0));
        }
    }
}

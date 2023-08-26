package constantan.lobotomy.common.entity.ai.sensor;

import constantan.lobotomy.common.init.ModMemoryModuleTypes;
import constantan.lobotomy.common.init.ModSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class LivingEntityInAoESensor<E extends LivingEntity> extends PredicateSensor<LivingEntity, E> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(
            ModMemoryModuleTypes.IN_AOE_LIVING_ENTITY.get(),
            ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get()
    );

    @Nullable
    protected Function<E, AABB> attackFunction = null;
    @Nullable
    protected Function<E, AABB> checkFunction = null;

    public LivingEntityInAoESensor() {
        super((aoeAttackMob, livingEntity) -> true);
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensors.LIVING_ENTITY_IN_AOE.get();
    }

    public LivingEntityInAoESensor<E> attackRange(Function<E, AABB> attackFunction) {
        this.attackFunction = attackFunction;
        return this;
    }

    public LivingEntityInAoESensor<E> checkRange(Function<E, AABB> checkFunction) {
        this.checkFunction = checkFunction;
        return this;
    }

    @Override
    protected void doTick(ServerLevel level, E aoeAttackMob) {
        AABB attackArea = this.attackFunction == null
                ? aoeAttackMob.getBoundingBox().inflate(1.0F)
                : this.attackFunction.apply(aoeAttackMob);
        AABB checkArea = this.checkFunction == null
                ? attackArea
                : this.checkFunction.apply(aoeAttackMob);

        List<LivingEntity> attackAreaEntities = EntityRetrievalUtil.getEntities(level, attackArea,
                entity -> entity instanceof LivingEntity livingEntity && predicate().test(livingEntity, aoeAttackMob));
        List<LivingEntity> checkAreaEntities = EntityRetrievalUtil.getEntities(level, checkArea,
                entity -> entity instanceof LivingEntity livingEntity && predicate().test(livingEntity, aoeAttackMob));

        attackAreaEntities.sort(Comparator.comparingDouble(aoeAttackMob::distanceToSqr));
        checkAreaEntities.sort(Comparator.comparingDouble(aoeAttackMob::distanceToSqr));

        BrainUtils.setMemory(aoeAttackMob, ModMemoryModuleTypes.IN_AOE_LIVING_ENTITY.get(), attackAreaEntities);
        BrainUtils.setMemory(aoeAttackMob, ModMemoryModuleTypes.IN_CHECK_AREA_LIVING_ENTITY.get(), checkAreaEntities);
    }
}

package constantan.lobotomy.common.entity.ai.behaviour;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.ToIntFunction;

public class SetPlayerTransientLookTarget<E extends LivingEntity> extends SetPlayerLookTarget<E> {

    protected ToIntFunction<E> expirationTicksFunction = entity -> entity.getRandom().nextInt(50, 100);

    public SetPlayerTransientLookTarget<E> expirationTicks(ToIntFunction<E> function) {
        this.expirationTicksFunction = function;
        return this;
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, true),
                expirationTicksFunction.applyAsInt(entity));
    }
}

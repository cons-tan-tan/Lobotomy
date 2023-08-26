package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SmartBrainAbnormalityEntity<T extends AbnormalityEntity<T> & SmartBrainOwner<T>> extends AbnormalityEntity<T>
        implements SmartBrainOwner<T> {

    public SmartBrainAbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void customServerAiStep() {
        this.tickBrain((T) this);
    }

    @NotNull
    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>((T) this);
    }

    @Override
    abstract public List<ExtendedSensor<T>> getSensors();

    @Override
    public abstract BrainActivityGroup<T> getCoreTasks();

    /**
     * ジェネリクスを省略しない
     */
    @Override
    public abstract BrainActivityGroup<T> getIdleTasks();

    @Override
    public abstract BrainActivityGroup<T> getFightTasks();
}

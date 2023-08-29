package constantan.lobotomy.common.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SmartBrainAbnormalityEntity<T extends AbnormalityEntity<T> & SmartBrainOwner<T>>
        extends AbnormalityEntity<T> implements SmartBrainOwner<T> {

    public SmartBrainAbnormalityEntity(EntityType<T> pEntityType, Level pLevel) {
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

    @Override
    public abstract BrainActivityGroup<T> getIdleTasks();

    @Override
    public abstract BrainActivityGroup<T> getFightTasks();


    @SafeVarargs
    protected final <M> ObjectArrayList<M> sensors(final M... sensors) {
        return new ObjectArrayList<M>(sensors);
    }

    @SafeVarargs
    protected final BrainActivityGroup<T> coreTasks(Behavior<? super T>... behaviours) {
        return BrainActivityGroup.<T>coreTasks(behaviours);
    }

    @SafeVarargs
    protected final BrainActivityGroup<T> idleTasks(Behavior<? super T>... behaviours) {
        return BrainActivityGroup.<T>idleTasks(behaviours);
    }

    @SafeVarargs
    protected final BrainActivityGroup<T> fightTasks(Behavior<? super T>... behaviours) {
        return BrainActivityGroup.<T>fightTasks(behaviours);
    }

    @SafeVarargs
    protected final FirstApplicableBehaviour<T> firstApplicableBehaviour(ExtendedBehaviour<? super T>... behaviours) {
        return new FirstApplicableBehaviour<T>(behaviours);
    }

    @SafeVarargs
    protected final OneRandomBehaviour<T> oneRandomBehaviour(ExtendedBehaviour<? super T>... behaviours) {
        return new OneRandomBehaviour<T>(behaviours);
    }
}

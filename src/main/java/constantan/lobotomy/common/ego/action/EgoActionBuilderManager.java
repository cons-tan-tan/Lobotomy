package constantan.lobotomy.common.ego.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EgoActionBuilderManager {

    private final List<EgoActionSequencer.Builder<?>> builderManager = new ArrayList<>();
    private final List<Float> factorManager = new ArrayList<>();
    private float factorSum = 0.0F;

    public EgoActionBuilderManager add(EgoActionSequencer.Builder<?> builder) {
        return this.add(builder, 100.0F);
    }

    public EgoActionBuilderManager add(EgoActionSequencer.Builder<?> builder, float factor) {
        this.builderManager.add(builder);
        this.factorSum += factor;
        this.factorManager.add(factorSum);
        return this;
    }

    public EgoActionSequencer.Builder<?> getActionBuilder() {
        float f = new Random().nextFloat(this.factorSum);
        for (int i = 0; i < this.factorManager.size(); i++) {
            if (factorManager.get(i) > f) {
                return builderManager.get(i);
            }
        }
        return builderManager.get(builderManager.size() - 1);
    }
}

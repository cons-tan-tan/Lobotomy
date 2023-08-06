package constantan.lobotomy.common.item;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class HeavenArmorItem extends EgoArmor implements IAnimatable {

    public HeavenArmorItem(String name, Properties builder) {
        super(name, builder);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }
}

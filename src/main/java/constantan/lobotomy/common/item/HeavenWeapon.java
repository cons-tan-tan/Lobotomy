package constantan.lobotomy.common.item;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class HeavenWeapon extends EgoMeleeWeapon implements IAnimatable {

    public HeavenWeapon(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }
}

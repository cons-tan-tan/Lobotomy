package constantan.lobotomy.common.item;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.network.ISyncable;

public class HeavenWeaponItem extends EgoMeleeWeaponItem implements IAnimatable, ISyncable {

    public HeavenWeaponItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public void onAnimationSync(int id, int state) {

    }
}

package constantan.lobotomy.common.item.ego;

import constantan.lobotomy.common.item.EgoMeleeWeapon;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class HeavenWeaponItem extends EgoMeleeWeapon implements IAnimatable {

    public HeavenWeaponItem(int minDamage, int maxDamage, Properties pProperties) {
        super(minDamage, maxDamage, pProperties);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }
}

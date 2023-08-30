package constantan.lobotomy.common.item;

public abstract class EgoRangeWeapon extends EgoWeapon {

    public EgoRangeWeapon(int minDamage, int maxDamage, Properties pProperties) {
        super(minDamage, maxDamage, pProperties);
    }

    public static class EgoRangeWeaponProperties extends EgoWeaponProperties<EgoRangeWeaponProperties> {
    }
}

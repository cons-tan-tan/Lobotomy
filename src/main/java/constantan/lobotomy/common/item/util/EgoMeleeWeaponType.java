package constantan.lobotomy.common.item.util;

public enum EgoMeleeWeaponType implements IEgoWeaponType{
    SWORD(0.0F, -2.4F, false),
    MACE(0.0F, -2.4F, false),
    AXE(-1.0F, -1.2F, false),
    SPEAR(1.0F, -1.2F, false),
    FIST(-2, 0, false),
    HAMMER(0.0F, -3.6F, true);

    private final float range;
    private final float speed;
    private final boolean twoHanded;

    EgoMeleeWeaponType(float range, float speed, boolean isTwoHanded) {
        this.range = range;
        this.speed = speed;
        this.twoHanded = isTwoHanded;
    }

    @Override
    public float getRange() {
        return this.range;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public boolean isTwoHanded() {
        return this.twoHanded;
    }
}

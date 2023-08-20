package constantan.lobotomy.common.item.util;

public enum EgoRangeWeaponType implements IEgoWeaponType{
    PISTOL(16F, -2.0F, false),
    CROSSBOW(24F, -3.5F, true),
    RIFLE(32F, -3.0F, true),
    CANNON(64F, -3.8F, true);

    private final float range;
    private final float speed;
    private final boolean twoHanded;

    EgoRangeWeaponType(float range, float speed, boolean isTwoHanded) {
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

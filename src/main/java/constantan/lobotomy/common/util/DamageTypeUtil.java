package constantan.lobotomy.common.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public enum DamageTypeUtil {
    RED,
    WHITE,
    BLACK,
    PALE;

    public DamageSource getEntityDamageSource(LivingEntity livingEntity) {
        DamageSource entityDamageSource = DamageSource.mobAttack(livingEntity);
        return switch (this) {
            case RED -> entityDamageSource;
            case WHITE -> entityDamageSource.bypassArmor();
            case BLACK -> entityDamageSource.bypassMagic();
            case PALE -> entityDamageSource.bypassArmor().bypassMagic();
        };
    }
}

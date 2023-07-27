package constantan.lobotomy.common.util;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.sanity.PlayerSanityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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

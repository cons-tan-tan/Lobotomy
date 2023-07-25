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

    //Paleダメージは額面通りのPaleダメージを受けてほしい
    //Player.hurtメソッド内の難易度によるダメージ量変更後に理想の値になるように調整するメソッド
    public static float adjustPlayerPaleDamageToIgnoreDifficulty(Difficulty difficulty, float f) {
        if (difficulty == Difficulty.EASY) {
            return Math.max(f, 2 * (f - 1.0F));
        } else if (difficulty == Difficulty.HARD) {
            return f * 2 / 3;
        }
        return f;
    }

    public boolean hurtLivingEntity(LivingEntity target, LivingEntity attacker, float amount) {
        if (amount <= 0) return false;
        Difficulty difficulty = attacker.level.getDifficulty();
        float ratio = RiskLevelUtil.getDamageRatio(target, attacker) * DefenseUtil.getDefense(target).get(this);
        float f = Math.abs(amount * ratio);
        if (ratio < 0) {//回復処理
            if (target instanceof Player player) {
                if (this == WHITE || this == BLACK) {//sanity回復
                    player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                        sanity.addSanityWithSync((int) f, (ServerPlayer) player);
                    });
                }
                if (this != WHITE) {//HP回復
                    if (this == PALE) {
                        player.heal(player.getMaxHealth() * f * 0.01F);
                    } else {
                        player.heal(f);
                    }
                }
            } else {
                if (this == PALE && !(target instanceof AbnormalityEntity)) {
                    target.heal(target.getMaxHealth() * f * 0.01F);
                } else {
                    target.heal(f);
                }
            }
            return false;
        } else {//ダメージ処理
            boolean flag = false;
            if (target instanceof Player player) {
                if (this == WHITE) {
                    //ラムダ式内で使う変数はfinal or 実質finalである必要があるのでfinalなbooleanの配列を経由している
                    final boolean[] flag_tmp = new boolean[1];
                    player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                        flag_tmp[0] = sanity.addSanityWithSync((int) -f, (ServerPlayer) player);
                    });
                    flag = flag_tmp[0];
                } else {
                    if (this == PALE) {
                        flag = player.hurt(this.getEntityDamageSource(attacker), adjustPlayerPaleDamageToIgnoreDifficulty(difficulty, player.getMaxHealth() * f * 0.01F));
                    } else {
                        flag = player.hurt(this.getEntityDamageSource(attacker), f);
                    }
                    if (this == BLACK && flag) {
                        player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                            sanity.addSanityWithSync((int) -f, (ServerPlayer) player);
                        });
                    }
                }
            } else {
                if (this == PALE && !(target instanceof AbnormalityEntity)) {
                    flag = target.hurt(this.getEntityDamageSource(attacker), target.getMaxHealth() * f * 0.01F);
                } else {
                    flag = target.hurt(this.getEntityDamageSource(attacker), f);
                }
            }
            return flag;
        }
    }
}

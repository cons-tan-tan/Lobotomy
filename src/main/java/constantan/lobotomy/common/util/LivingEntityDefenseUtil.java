package constantan.lobotomy.common.util;

import constantan.lobotomy.common.entity.Abnormality;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class LivingEntityDefenseUtil {

    public static final Map<DamageTypeUtil, Float> DEFAULT_ENTITY_DEFENSE = createDefense(1.0F, 1.0F, 1.5F, 2.0F);

    public static Map<DamageTypeUtil, Float> createDefense(float red, float white, float black, float pale) {
        return Map.of(DamageTypeUtil.RED, red, DamageTypeUtil.WHITE, white, DamageTypeUtil.BLACK, black, DamageTypeUtil.PALE, pale);
    }

    public static Map<DamageTypeUtil, Float> getLivingEntityDefense(LivingEntity livingEntity) {
        return DEFAULT_ENTITY_DEFENSE;
    }

    public static Map<DamageTypeUtil, Float> getDefense(LivingEntity livingEntity) {
        if (livingEntity instanceof Abnormality abnormality) {
            return abnormality.getDefense();
        } if (livingEntity instanceof Player player) {

            //PlayerのスーツEGOの耐性を取得

        }
        return getLivingEntityDefense(livingEntity);
    }
}

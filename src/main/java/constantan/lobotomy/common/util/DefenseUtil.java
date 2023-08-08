package constantan.lobotomy.common.util;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.item.EgoArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class DefenseUtil {

    public static final Map<DamageTypeUtil, Float> DEFAULT_DEFENSE = createDefense(1.0F, 1.0F, 1.5F, 2.0F);

    public static Map<DamageTypeUtil, Float> createDefense(float red, float white, float black, float pale) {
        return Map.of(DamageTypeUtil.RED, red, DamageTypeUtil.WHITE, white, DamageTypeUtil.BLACK, black, DamageTypeUtil.PALE, pale);
    }

    public static Map<DamageTypeUtil, Float> getLivingEntityDefense(LivingEntity livingEntity) {
        return DEFAULT_DEFENSE;
    }

    public static Map<DamageTypeUtil, Float> getDefense(LivingEntity livingEntity) {
        if (livingEntity instanceof AbnormalityEntity abnormalityEntity) {
            return abnormalityEntity.getAbnormalDefense();
        } if (livingEntity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EgoArmor egoArmor) {
                return egoArmor.getAbnormalDefense();
            }
        }
        return getLivingEntityDefense(livingEntity);
    }
}

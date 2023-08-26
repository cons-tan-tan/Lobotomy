package constantan.lobotomy.common.util;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.item.EgoArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
        if (livingEntity instanceof AbnormalityEntity<?> abnormalityEntity) {
            return abnormalityEntity.getAbnormalDefense();
        } if (livingEntity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EgoArmor egoArmor) {
                return egoArmor.getAbnormalDefense();
            }
        }
        return getLivingEntityDefense(livingEntity);
    }

    public static TextComponent getDefenseMultiplierTextComponent(Map<DamageTypeUtil, Float> defense) {
        return getDefenseMultiplierTextComponent(defense, false);
    }

    public static TextComponent getDefenseMultiplierTextComponent(Map<DamageTypeUtil, Float> defense, boolean isHighlighted) {
        TextComponent tooltip = new TextComponent("");

        for (DamageTypeUtil damageType : DamageTypeUtil.values()) {
            float multiplier = defense.get(damageType);
            String s = String.format("%.1f", multiplier);
            MutableComponent component = new TextComponent(s).withStyle(damageType.getColor());

            if (isHighlighted) {
                if (multiplier > 1) {
                    tooltip.append(component.copy().withStyle(ChatFormatting.BOLD));
                } else if (multiplier <= 0) {
                    tooltip.append(component.copy().withStyle(ChatFormatting.UNDERLINE));
                } else {
                    tooltip.append(component.copy());
                }
            } else {
                tooltip.append(component.copy());
            }

            if (damageType != DamageTypeUtil.PALE) {
                tooltip.append(" ");
            }
        }

        return (TextComponent) new TextComponent("").append(
                new TranslatableComponent("attribute.modifier.equals.0",
                tooltip, new TranslatableComponent("attribute.lobotomy.name.ego_armor.defense_multiplier").withStyle(ChatFormatting.BLUE)));
    }
}

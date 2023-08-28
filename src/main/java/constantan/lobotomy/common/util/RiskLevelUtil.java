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

public enum RiskLevelUtil {
    ZAYIN(1, ChatFormatting.GREEN),
    TETH(2, ChatFormatting.AQUA),
    HE(3, ChatFormatting.YELLOW),
    WAW(4, ChatFormatting.DARK_PURPLE),
    ALEPH(5, ChatFormatting.DARK_RED);

    private final int level;
    private final ChatFormatting color;

    RiskLevelUtil(int level, ChatFormatting color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return this.level;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public TextComponent getColoredTextComponent() {
        MutableComponent component = new TextComponent(this.name())
                .withStyle(this.getColor()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BOLD);
        return (TextComponent) new TextComponent("")
                .append(new TranslatableComponent("tooltip.lobotomy.risk_level", new TranslatableComponent("word.lobotomy.risk_level"), component));
    }

    public static float getDamageRatio(RiskLevelUtil defenderRiskLevel, RiskLevelUtil attackerRiskLevel) {
        return switch (defenderRiskLevel.getLevel() - attackerRiskLevel.getLevel()) {
            case 4 -> 0.4F;
            case 3 -> 0.6F;
            case 2 -> 0.7F;
            case 1 -> 0.8F;
            default -> 1.0F;
            case -2 -> 1.2F;
            case -3 -> 1.5F;
            case -4 -> 2.0F;
        };
    }

    public static RiskLevelUtil getRiskLevel(LivingEntity livingEntity) {
        if (livingEntity instanceof AbnormalityEntity<?> abnormalityEntity) {
            return abnormalityEntity.getRiskLevel();
        } else if (livingEntity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EgoArmor egoArmor) {
                return egoArmor.getRiskLevel();
            }
        }
        return getLivingEntityRiskLevel(livingEntity);
    }

    private static RiskLevelUtil getLivingEntityRiskLevel(LivingEntity livingEntity) {
        return ZAYIN;
    }
}

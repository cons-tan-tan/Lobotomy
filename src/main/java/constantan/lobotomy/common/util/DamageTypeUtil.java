package constantan.lobotomy.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public enum DamageTypeUtil {
    RED(ChatFormatting.DARK_RED),
    WHITE(ChatFormatting.WHITE),
    BLACK(ChatFormatting.DARK_GRAY),
    PALE(ChatFormatting.DARK_AQUA);

    final ChatFormatting color;

    DamageTypeUtil(ChatFormatting color) {
        this.color = color;
    }

    public boolean canAffectSanity() {
        return this == WHITE || this == BLACK;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public TextComponent getColoredTextComponent() {
        return (TextComponent) new TextComponent(this.name()).withStyle(this.getColor());
    }

    public TextComponent getColoredTextComponentWithValue(int min, int max, boolean isProjectile) {
        return (TextComponent) new TextComponent("").append(new TranslatableComponent("attribute.modifier.equals.0",
                new TextComponent(this.name()).withStyle(this.getColor()).append("(" + min + "-" + max + ")"),
                isProjectile
                        ? new TranslatableComponent("attribute.lobotomy.name.ego_armor.projectile_damage").withStyle(ChatFormatting.GOLD)
                        : new TranslatableComponent("attribute.name.generic.attack_damage").withStyle(ChatFormatting.DARK_GREEN)));
    }
}

package constantan.lobotomy.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

public enum DamageTypeUtil {
    RED,
    WHITE,
    BLACK,
    PALE;

    public ChatFormatting getColor() {
        return switch (this) {
            case RED ->  ChatFormatting.DARK_RED;
            case WHITE -> ChatFormatting.WHITE;
            case BLACK -> ChatFormatting.DARK_GRAY;
            case PALE -> ChatFormatting.DARK_AQUA;
        };
    }

    public MutableComponent getColoredTextComponent() {
        MutableComponent component;
        component = switch (this) {
            case RED -> new TextComponent("RED").withStyle(this.getColor());
            case WHITE -> new TextComponent("WHITE").withStyle(this.getColor());
            case BLACK -> new TextComponent("BLACK").withStyle(this.getColor());
            case PALE -> new TextComponent("PALE").withStyle(this.getColor());
        };
        return component;
    }
}

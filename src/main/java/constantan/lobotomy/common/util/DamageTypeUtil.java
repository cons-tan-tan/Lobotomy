package constantan.lobotomy.common.util;

import net.minecraft.ChatFormatting;

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
}

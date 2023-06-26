package constantan.lobotomy.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class LibDebug {
    private static Minecraft minecraft;

    public LibDebug() {
        minecraft = Minecraft.getInstance();
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public void addChatMessage(String text) {
        Player player = minecraft.player;
        player.sendMessage(new TextComponent("[Lobotomy] " + text), player.getUUID());
    }
}

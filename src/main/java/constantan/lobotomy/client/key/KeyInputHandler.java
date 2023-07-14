package constantan.lobotomy.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;

public class KeyInputHandler {

    public static void onKeyInput(InputEvent event) {
        if (KeyBindings.checkSanityKeyMapping.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            player.displayClientMessage(new TextComponent("key detected"), true);
        }
    }
}

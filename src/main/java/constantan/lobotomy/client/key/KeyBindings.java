package constantan.lobotomy.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.awt.event.KeyEvent;

public class KeyBindings {

    public static final String KEY_CATEGORIES_LOBOTOMY = "key.categories.lobotomy";
    public static final String KEY_CHECK_SANITY = "key.lobotomy.check_sanity";

    public static KeyMapping checkSanityKeyMapping = new KeyMapping(KEY_CHECK_SANITY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, KeyEvent.VK_PERIOD, KEY_CATEGORIES_LOBOTOMY);

    public static void init() {
        ClientRegistry.registerKeyBinding(checkSanityKeyMapping);
    }
}

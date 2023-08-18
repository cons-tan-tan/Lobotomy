package constantan.lobotomy.common.event;

import constantan.lobotomy.common.command.LobotomyCommands;
import constantan.lobotomy.lib.LibMisc;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LobotomyCommands.register(event.getDispatcher());
    }
}

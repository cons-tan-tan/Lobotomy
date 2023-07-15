package constantan.lobotomy;

import constantan.lobotomy.client.ClientSetup;
import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.lib.LibMisc;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LibMisc.MOD_ID)
public class LobotomyMod {
    public static Logger logger = LoggerFactory.getLogger(LibMisc.MOD_NAME);

    public LobotomyMod() {
        ModSetup.setup();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init));
    }
}

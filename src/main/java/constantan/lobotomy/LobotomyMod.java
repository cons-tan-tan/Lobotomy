package constantan.lobotomy;

import constantan.lobotomy.client.ClientSetup;
import constantan.lobotomy.common.init.ModBlocks;
import constantan.lobotomy.common.init.ModEffects;
import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

import javax.annotation.Nonnull;

@Mod(LibMisc.MOD_ID)
public class LobotomyMod {
    public static Logger logger = LoggerFactory.getLogger(LibMisc.MOD_NAME);

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(LibMisc.MOD_ID)
    {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GIANT_TREE_SAP.get());
        }
    };

    public LobotomyMod() {

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }
}

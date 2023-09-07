package constantan.lobotomy.common;

import constantan.lobotomy.common.init.*;
import constantan.lobotomy.common.ego.gift.GiftCuriosSlotType;
import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.config.LobotomyClientConfigs;
import constantan.lobotomy.config.LobotomyCommonConfigs;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(LibMisc.MOD_ID)
    {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GIANT_TREE_SAP.get());
        }
    };

    public static void setup() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);
        ModMemoryModuleTypes.MEMORY_MODULE_TYPES.register(modEventBus);
        modEventBus.addListener(ModEntityTypes::entityAttributeEvent);
        modEventBus.addListener(ModSetup::enqueueIMC);

        GeckoLib.initialize();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, LobotomyClientConfigs.SPEC, "lobotomy-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LobotomyCommonConfigs.SPEC, "lobotomy-common.toml");
    }

    public static void init(final FMLCommonSetupEvent event) {
        Messages.register();
    }

    private static void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> GiftCuriosSlotType.EYE.getMessageBuilder().build());
    }
}

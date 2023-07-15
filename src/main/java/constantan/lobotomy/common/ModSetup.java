package constantan.lobotomy.common;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.common.init.ModBlocks;
import constantan.lobotomy.common.init.ModEffects;
import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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

        GeckoLib.initialize();
    }

    public static void init(final FMLCommonSetupEvent event) {
        Messages.register();
    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdEntity.setAttributes());
    }
}

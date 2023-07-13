package constantan.lobotomy.common.event;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.lib.LibMisc;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdEntity.setAttributes());
    }
}

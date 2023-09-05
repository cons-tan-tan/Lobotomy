package constantan.lobotomy.common.event;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;
import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class EgoActionEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            IMixinPlayer iMixinPlayer = (IMixinPlayer) player;
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (iMixinPlayer.hasEgoActionSequencer(equipmentSlot)) {
                    EgoActionSequencer<?> sequencer = iMixinPlayer.getEgoActionSequencer(equipmentSlot);
                    ItemStack stack = player.getItemBySlot(equipmentSlot);
                    if (sequencer.getStack().equals(stack)) {
                        sequencer.tick(player);
                    } else {
                        iMixinPlayer.removeEgoActionSequencer(equipmentSlot);
                    }
                }
            }
        }
    }
}

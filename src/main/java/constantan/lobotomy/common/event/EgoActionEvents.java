package constantan.lobotomy.common.event;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;
import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.InteractionHand;
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
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            IMixinPlayer iMixinPlayer = (IMixinPlayer) player;
            if (iMixinPlayer.hasEgoActionSequencer()) {
                EgoActionSequencer sequencer = iMixinPlayer.getEgoActionSequencer();
                if (sequencer.getStack().equals(stack)) {
                    iMixinPlayer.getEgoActionSequencer().tick(player);
                } else {
                    iMixinPlayer.removeEgoActionSequencer();
                }
            }
        }
    }


}

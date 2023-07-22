package constantan.lobotomy.common.event;

import constantan.lobotomy.common.sanity.PlayerSanity;
import constantan.lobotomy.common.sanity.PlayerSanityProvider;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class PlayerSanityEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player) {
            if(!player.getCapability(PlayerSanityProvider.PLAYER_SANITY).isPresent()) {
                event.addCapability(new ResourceLocation(LibMisc.MOD_ID, "properties"), new PlayerSanityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(oldStore -> {
                Player new_player = event.getPlayer();
                new_player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    //ついでにSanity全回復
                    newStore.setSanity(newStore.getMaxSanity());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.isEndConquered()) {
            Player player = event.getPlayer();
            player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                //onPlayerClonedで全回復したSanityのクライアント同期
                //onPlayerCloned時点で送っても同期されなかったため
                sanity.syncClientData((ServerPlayer) player);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
            sanity.syncClientData((ServerPlayer) player);
        });
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSanity.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                if (sanity.getSanity() < sanity.getMaxSanity() && player.getRandom().nextFloat() < 0.005f) {
                    sanity.addSanityWithSync(1, (ServerPlayer) player);
                }
            });
        }
    }
}

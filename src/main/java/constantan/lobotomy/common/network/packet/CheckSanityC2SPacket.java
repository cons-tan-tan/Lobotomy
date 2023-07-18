package constantan.lobotomy.common.network.packet;

import constantan.lobotomy.common.sanity.PlayerSanityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CheckSanityC2SPacket {
    public CheckSanityC2SPacket() {

    }

    public CheckSanityC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context content = supplier.get();
        content.enqueueWork(() -> {
            //サーバー上での処理
            ServerPlayer player = content.getSender();
            ServerLevel level = player.getLevel();
            player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                player.sendMessage(new TextComponent("Server"), player.getUUID());
                player.sendMessage(new TextComponent("Sanity:" + sanity.getSanity()), player.getUUID());
                player.sendMessage(new TextComponent("MaxSanity:" + sanity.getMaxSanity()), player.getUUID());
            });
        });
        return true;
    }
}

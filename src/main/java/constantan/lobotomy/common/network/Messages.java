package constantan.lobotomy.common.network;

import constantan.lobotomy.common.network.packet.CheckSanityC2SPacket;
import constantan.lobotomy.common.network.packet.SyncSanityS2CPacket;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(LibMisc.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(CheckSanityC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CheckSanityC2SPacket::new)
                .encoder(CheckSanityC2SPacket::toBytes)
                .consumer(CheckSanityC2SPacket::handle)
                .add();

        net.messageBuilder(SyncSanityS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSanityS2CPacket::new)
                .encoder(SyncSanityS2CPacket::toBytes)
                .consumer(SyncSanityS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

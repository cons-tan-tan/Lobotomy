package constantan.lobotomy.common.network.packet;

import constantan.lobotomy.client.gui.ClientSanityData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSanityS2CPacket {
    private final int sanity;

    public SyncSanityS2CPacket(int sanity) {
        this.sanity = sanity;
    }

    public SyncSanityS2CPacket(FriendlyByteBuf buf) {
        this.sanity = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(sanity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context content = supplier.get();
        content.enqueueWork(() -> {
            //クライアント上での処理
            ClientSanityData.setPlayerSanity(sanity);
        });
        return true;
    }
}

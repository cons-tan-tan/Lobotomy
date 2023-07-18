package constantan.lobotomy.common.network.packet;

import constantan.lobotomy.client.gui.ClientSanityData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSanityS2CPacket {
    private final int sanity;
    private final int maxSanity;

    public SyncSanityS2CPacket(int sanity, int max_sanity) {
        this.sanity = sanity;
        this.maxSanity = max_sanity;
    }

    public SyncSanityS2CPacket(FriendlyByteBuf buf) {
        this.sanity = buf.readInt();
        this.maxSanity = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.sanity);
        buf.writeInt(this.maxSanity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context content = supplier.get();
        content.enqueueWork(() -> {
            //クライアント上での処理
            ClientSanityData.setPlayerSanity(this.sanity);
            ClientSanityData.setPlayerMaxSanity(this.maxSanity);
        });
        return true;
    }
}

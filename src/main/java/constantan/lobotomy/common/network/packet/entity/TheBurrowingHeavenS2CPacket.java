package constantan.lobotomy.common.network.packet.entity;

import constantan.lobotomy.common.entity.custom.TheBurrowingHeaven;
import constantan.lobotomy.common.network.Messages;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class TheBurrowingHeavenS2CPacket {
    private final UUID theBurrowingHeavenUUID;
    private final UUID playerUUID;

    public TheBurrowingHeavenS2CPacket(UUID theBurrowingHeavenUUID, UUID playerUUID) {
        this.theBurrowingHeavenUUID = theBurrowingHeavenUUID;
        this.playerUUID = playerUUID;
    }

    public TheBurrowingHeavenS2CPacket(FriendlyByteBuf buf) {
        this.theBurrowingHeavenUUID = buf.readUUID();
        this.playerUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.theBurrowingHeavenUUID);
        buf.writeUUID(this.playerUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context content = supplier.get();
        content.enqueueWork(() -> {
            //クライアント上での処理
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                public final UUID theBurrowingHeavenUUID = TheBurrowingHeavenS2CPacket.this.theBurrowingHeavenUUID;
                @Override
                public void run() {
                    Player player = Minecraft.getInstance().player;
                    Vec3 pos = player.getPosition(1.0F);
                    float r = TheBurrowingHeaven.SEARCH_RANGE + 4.0F;
                    AABB searchArea = new AABB(pos.x - r, pos.y - r, pos.z - r, pos.x + r, pos.y + r, pos.z + r);
                    List<TheBurrowingHeaven> listTheBurrowingHeaven = player.level.getEntitiesOfClass(TheBurrowingHeaven.class, searchArea);
                    for (TheBurrowingHeaven theBurrowingHeaven : listTheBurrowingHeaven) {
                        if (theBurrowingHeaven.getUUID().equals(this.theBurrowingHeavenUUID) && theBurrowingHeaven.clientShouldRender) {
                            Messages.sendToServer(new TheBurrowingHeavenC2SPacket(this.theBurrowingHeavenUUID));
                        }
                    }
                }
                });
        });
        return true;
    }
}

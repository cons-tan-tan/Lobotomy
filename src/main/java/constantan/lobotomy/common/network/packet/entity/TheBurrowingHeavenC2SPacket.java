package constantan.lobotomy.common.network.packet.entity;

import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class TheBurrowingHeavenC2SPacket {
    private final UUID theBurrowingHeavenUUID;

    public TheBurrowingHeavenC2SPacket(UUID theBurrowingHeavenUUID) {
        this.theBurrowingHeavenUUID = theBurrowingHeavenUUID;
    }

    public TheBurrowingHeavenC2SPacket(FriendlyByteBuf buf) {
        this.theBurrowingHeavenUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.theBurrowingHeavenUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context content = supplier.get();
        content.enqueueWork(() -> {
            //サーバー上での処理
            ServerPlayer player = content.getSender();
            Vec3 pos = player.getPosition(1.0F);
            float r = TheBurrowingHeavenEntity.SEARCH_RANGE + 8.0F;
            AABB searchArea = new AABB(pos.x - r, pos.y - r, pos.z - r, pos.x + r, pos.y + r, pos.z + r);
            List<TheBurrowingHeavenEntity> listTheBurrowingHeaven = player.level.getEntitiesOfClass(TheBurrowingHeavenEntity.class, searchArea);
            for (TheBurrowingHeavenEntity theBurrowingHeaven : listTheBurrowingHeaven) {
                if (theBurrowingHeaven.getUUID().equals(this.theBurrowingHeavenUUID)) {
                    theBurrowingHeaven.serverSeen = true;
                }
            }
        });
        return true;
    }
}

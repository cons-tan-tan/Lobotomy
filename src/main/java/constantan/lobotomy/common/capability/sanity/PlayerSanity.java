package constantan.lobotomy.common.capability.sanity;

import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.common.network.packet.sanity.SyncSanityS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public class PlayerSanity {
    public static final int MIN_SANITY = 0;
    public static final int DEFAULT_SANITY = 20;

    private int sanity;
    private int maxSanity;

    public PlayerSanity() {
        this.maxSanity = DEFAULT_SANITY;
        this.sanity = this.maxSanity;
    }

    public int getSanity() {
        return this.sanity;
    }

    public int getMaxSanity() {
        return this.maxSanity;
    }

    public int getMinSanity() {
        return MIN_SANITY;
    }


    /**
     * 指定の値にサーバー側でSanityの値を変更する<br>
     * クライアント同期の場合は{@link PlayerSanity#setSanityWithSync(int, ServerPlayer)}を使用
     * @param sanity Sanityの値
     * @return Sanityが変更されたかどうか
     */
    public boolean setSanity(int sanity) {
        if (sanity == this.sanity) return false;
        this.sanity = Mth.clamp(sanity, MIN_SANITY, this.maxSanity);
        return true;
    }

    /**
     * クライアント同期を伴うsetSanityメソッド
     * @param sanity Sanityの値
     * @param player 同期パケットを送るプレイヤー
     */
    public boolean setSanityWithSync(int sanity, ServerPlayer player) {
        boolean flag = this.setSanity(sanity);
        if (flag) {
            this.syncClientData(player);
        }
        return flag;
    }

    public boolean addSanity(int add) {
        return this.setSanity(this.sanity + add);
    }

    public boolean addSanityWithSync(int add, ServerPlayer player) {
        boolean flag = this.addSanity(add);
        if (flag) {
            this.syncClientData(player);
        }
        return flag;
    }

    private boolean setMaxSanity(int max_sanity) {
        if (max_sanity == this.maxSanity) return false;
        this.maxSanity = Math.max(max_sanity, MIN_SANITY + 1);
        return true;
    }

    public boolean setMaxSanityWithSync(int max_sanity, ServerPlayer player) {
        boolean flag = setMaxSanity(max_sanity);
        if (flag) {
            this.syncClientData(player);
        }
        return flag;
    }

    private boolean addMaxSanity(int max_sanity) {
        return this.setMaxSanity(this.maxSanity + max_sanity);
    }

    public boolean addMaxSanityWithSync(int max_sanity, ServerPlayer player) {
        boolean flag = this.addMaxSanity(max_sanity);
        if (flag) {
            this.syncClientData(player);
        }
        return flag;
    }

    public void syncClientData(ServerPlayer player) {
        //クライアント同期用のパケットを送る
        Messages.sendToPlayer(new SyncSanityS2CPacket(this.sanity, this.maxSanity), player);
    }

    public void copyFrom(PlayerSanity source) {
        this.maxSanity = source.maxSanity;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("max_sanity", this.maxSanity);
        nbt.putInt("sanity", this.sanity);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.maxSanity = nbt.getInt("max_sanity");
        this.sanity = nbt.getInt("sanity");
    }
}

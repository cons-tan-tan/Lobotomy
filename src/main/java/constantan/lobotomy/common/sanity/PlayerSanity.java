package constantan.lobotomy.common.sanity;

import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.common.network.packet.SyncSanityS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerSanity {
    private final int MIN_SANITY = 0;
    private final int DEFAULT_SANITY = 20;

    private int sanity;
    private int maxSanity;

    public PlayerSanity() {
        this.maxSanity = this.DEFAULT_SANITY;
        this.sanity = this.maxSanity;
    }

    public int getSanity() {
        return this.sanity;
    }

    public int getMaxSanity() {
        return this.maxSanity;
    }

    public int getMinSanity() {
        return this.MIN_SANITY;
    }


    /**
     * 指定の値にサーバー側でSanityの値を変更する<br>
     * クライアント同期の場合は{@link PlayerSanity#setSanityWithSync(int, ServerPlayer)}を使用
     * @param sanity Sanityの値
     * @return Sanityが変更されたかどうか
     */
    public boolean setSanity(int sanity) {
        if (sanity == this.sanity) {
            return false;
        } else if (sanity < this.MIN_SANITY) {
            this.sanity = this.MIN_SANITY;
        } else {
            this.sanity = Math.min(sanity, this.maxSanity);
        }
        return true;
    }

    /**
     * クライアント同期を伴うsetSanityメソッド
     * @param sanity Sanityの値
     * @param player 同期パケットを送るプレイヤー
     */
    public void setSanityWithSync(int sanity, ServerPlayer player) {
        if (this.setSanity(sanity)) {
            this.syncClientData(player);
        }
    }

    public boolean addSanity(int add) {
        return this.setSanity(this.sanity + add);
    }

    public void addSanityWithSync(int add, ServerPlayer player) {
        if (this.addSanity(add)) {
            this.syncClientData(player);
        }
    }

    private boolean setMaxSanity(int max_sanity) {
        if (max_sanity != this.maxSanity && max_sanity > this.MIN_SANITY) {
            this.maxSanity = max_sanity;
            return true;
        }
        return false;
    }

    public void setMaxSanity(int max_sanity, ServerPlayer player) {
        if (setMaxSanity(max_sanity)) {
            this.syncClientData(player);
        }
    }

    private boolean addMaxSanity(int max_sanity) {
        return this.setMaxSanity(max_sanity);
    }

    public void addMaxSanity(int max_sanity, ServerPlayer player) {
        if (this.addMaxSanity(max_sanity)) {
            this.syncClientData(player);
        }
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

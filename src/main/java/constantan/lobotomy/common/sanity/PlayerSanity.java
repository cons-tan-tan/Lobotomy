package constantan.lobotomy.common.sanity;

import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.common.network.packet.SyncSanityS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerSanity {
    private int sanity;
    private final int MIN_SANITY = 0;
    private final int MAX_SANITY = 10;

    public int getSanity() {
        return this.sanity;
    }

    public int getMaxSanity() {
        return this.MAX_SANITY;
    }

    public int getMinSanity() {
        return this.MIN_SANITY;
    }


    /**
     * 指定の値にサーバー側でSanityの値を変更する<br>
     * クライアント同期の場合は{@link PlayerSanity#setSanity(int, ServerPlayer)}を使用
     * @param sanity Sanityの値
     * @return Sanityが変更されたかどうか
     */
    private boolean setSanity(int sanity) {
        if (sanity == this.sanity) {
            return false;
        } else if (sanity < this.MIN_SANITY) {
            this.sanity = this.MIN_SANITY;
        } else {
            this.sanity = Math.min(sanity, this.MAX_SANITY);
        }
        return true;
    }

    /**
     * クライアント同期を伴うsetSanityメソッド
     * @param sanity Sanityの値
     * @param player 同期パケットを送るプレイヤー
     */
    public void setSanity(int sanity, ServerPlayer player) {
        if (this.setSanity(sanity)) {
            syncWithClient(player);
        }
    }

    /**
     * 指定の値だけサーバー側のSanityを増減させる<br>
     * クライアント同期の場合は{@link PlayerSanity#addSanity(int, ServerPlayer)}を使用
     * @param add 追加するSanityの値 負の値なら減らす
     * @return Sanityが変更されたかどうか
     */
    private boolean addSanity(int add) {
        return this.setSanity(this.sanity + add);
    }

    /**
     * クライアント同期を伴うaddSanityメソッド
     * @param add 追加するSanityの値 負の値なら減らす
     * @param player 同期パケットを送るプレイヤー
     */
    public void addSanity(int add, ServerPlayer player) {
        if (this.addSanity(add)) {
            syncWithClient(player);
        }
    }

    public void syncWithClient(ServerPlayer player) {
        Messages.sendToPlayer(new SyncSanityS2CPacket(this.sanity), player);
    }

    public void copyFrom(PlayerSanity source) {
        this.sanity = source.sanity;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("sanity", this.sanity);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.sanity = nbt.getInt("sanity");
    }
}

package constantan.lobotomy.common.sanity;

import net.minecraft.nbt.CompoundTag;

public class PlayerSanity {
    private int sanity;
    private final int MIN_SANITY = 0;
    private final int MAX_SANITY = 100;

    public int getSanity() {
        return this.sanity;
    }

    public int getMaxSanity() {
        return this.MAX_SANITY;
    }

    public void setSanity(int sanity) {
        if (sanity < this.MIN_SANITY) {
            this.sanity = this.MIN_SANITY;
        } else {
            this.sanity = Math.min(sanity, this.MAX_SANITY);
        }
    }

    public boolean addSanity(int add) {
        if (add > 0) {
            this.sanity = Math.min(this.sanity + add, this.MAX_SANITY);
            return true;
        }
        return false;
    }

    public boolean subSanity(int sub) {
        if (sub > 0) {
            this.sanity = Math.max(this.sanity - sub, this.MIN_SANITY);
            return true;
        }
        return false;
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

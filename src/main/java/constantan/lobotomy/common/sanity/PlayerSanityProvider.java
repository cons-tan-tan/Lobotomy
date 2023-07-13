package constantan.lobotomy.common.sanity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSanityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerSanity> PLAYER_SANITY = CapabilityManager.get(new CapabilityToken<PlayerSanity>() { });

    private PlayerSanity sanity = null;
    private final LazyOptional<PlayerSanity> optional = LazyOptional.of(this::createPlayerSanity);

    private PlayerSanity createPlayerSanity() {
        if (this.sanity == null) {
            this.sanity = new PlayerSanity();
        }

        return this.sanity;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SANITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.createPlayerSanity().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSanity().loadNBTData(nbt);
    }
}

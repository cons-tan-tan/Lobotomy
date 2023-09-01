package constantan.lobotomy.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class CommonPartEntity<R extends Entity> extends PartEntity<R> {

    public final String name;
    private final EntityDimensions size;

    public CommonPartEntity(R parent, String name, float width, float height) {
        super(parent);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        return !this.isInvulnerableTo(pSource) && this.getParent().hurt(pSource, pAmount);
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        this.getParent().push(pX, pY, pZ);
    }

    @Override
    public boolean is(@NotNull Entity pEntity) {
        return this == pEntity || this.getParent() == pEntity;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public static AtomicInteger getEntityCounter() {
        return ENTITY_COUNTER;
    }
}

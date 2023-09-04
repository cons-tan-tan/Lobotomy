package constantan.lobotomy.integration.jade;

import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.impl.EntityAccessorImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class DummyEntityAccessorImpl extends EntityAccessorImpl {

    public DummyEntityAccessorImpl(Entity entity, Level level, Player player, CompoundTag serverData, EntityHitResult hit, boolean serverConnected) {
        super(entity, level, player, serverData, hit, serverConnected);
    }

    @Override
    public boolean shouldDisplay() {
        return false;
    }

    public static DummyEntityAccessorImpl copyFrom(EntityAccessor entityAccessor) {
        return new DummyEntityAccessorImpl(
                entityAccessor.getEntity(),
                entityAccessor.getLevel(),
                entityAccessor.getPlayer(),
                entityAccessor.getServerData(),
                entityAccessor.getHitResult(),
                entityAccessor.isServerConnected()
        );
    }
}

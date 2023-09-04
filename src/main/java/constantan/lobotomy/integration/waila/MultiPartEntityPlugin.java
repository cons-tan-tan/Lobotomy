package constantan.lobotomy.integration.waila;

import constantan.lobotomy.common.entity.CommonPartEntity;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.event.WailaRayTraceEvent;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("UnstableApiUsage")
@WailaPlugin
public class MultiPartEntityPlugin implements IWailaPlugin {

    private static IWailaClientRegistration client;

    public MultiPartEntityPlugin() {
        MinecraftForge.EVENT_BUS.addListener(this::overrideCommonPartEntity);
    }

    public void overrideCommonPartEntity(WailaRayTraceEvent event) {
        Accessor<?> accessor = event.getAccessor();
        if (accessor instanceof EntityAccessor entityAccessor) {
            if (entityAccessor.getEntity() instanceof CommonPartEntity<?> commonPartEntity) {
                accessor = client.createEntityAccessor(
                        commonPartEntity.getParent(),
                        accessor.getLevel(),
                        accessor.getPlayer(),
                        commonPartEntity.getParent().getPersistentData(),
                        entityAccessor.getHitResult(),
                        accessor.isServerConnected()
                );
                event.setAccessor(accessor);
            }
        }
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        client = registration;
    }
}

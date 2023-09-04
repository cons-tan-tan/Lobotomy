package constantan.lobotomy.integration.jade;

import constantan.lobotomy.common.entity.CommonPartEntity;
import constantan.lobotomy.common.entity.IMultiPart;
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
                event.setAccessor(client.createEntityAccessor(
                        commonPartEntity.getParent(),
                        entityAccessor.getLevel(),
                        entityAccessor.getPlayer(),
                        commonPartEntity.getParent().getPersistentData(),
                        entityAccessor.getHitResult(),
                        entityAccessor.isServerConnected()
                ));
            } else if (entityAccessor.getEntity() instanceof IMultiPart<?,?>) {
                event.setAccessor(DummyEntityAccessorImpl.copyFrom(entityAccessor));
            }
        }
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        client = registration;
    }
}

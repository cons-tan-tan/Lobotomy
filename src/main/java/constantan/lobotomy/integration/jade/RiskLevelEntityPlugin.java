package constantan.lobotomy.integration.jade;

import mcp.mobius.waila.api.*;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("UnstableApiUsage")
@WailaPlugin
public class RiskLevelEntityPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerComponentProvider(RiskLevelComponentProvider.INSTANCE, TooltipPosition.BODY, LivingEntity.class);
    }
}

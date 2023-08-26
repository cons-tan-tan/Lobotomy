package constantan.lobotomy.common.entity.ai.behaviour;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;

public class FloatToSurfaceOfFluidWithSafety<E extends Mob> extends FloatToSurfaceOfFluid<E> {

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity) && entity.getNavigation().canFloat();
    }
}

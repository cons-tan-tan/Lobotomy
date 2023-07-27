package constantan.lobotomy.common.util.mixin;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public interface IMixinEntityType<T extends Entity> {

    IMixinEntityType<T> riskLevel(RiskLevelUtil riskLevel);

    IMixinEntityType<T> damageType(DamageTypeUtil defaultDamageType);

    IMixinEntityType<T> defense(float red, float white, float black, float pale);

    EntityType<T> build();

    RiskLevelUtil getRiskLevel();

    DamageTypeUtil getDamageType();

    Map<DamageTypeUtil, Float> getDefense();
}

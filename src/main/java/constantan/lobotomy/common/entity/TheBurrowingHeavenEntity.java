package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TheBurrowingHeavenEntity extends Monster implements IAnimatable {

    protected static final AnimationBuilder IDLE = new AnimationBuilder()
            .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);

    private final AnimationFactory FACTORY = GeckoLibUtil.createFactory(this);

    protected TheBurrowingHeavenEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}

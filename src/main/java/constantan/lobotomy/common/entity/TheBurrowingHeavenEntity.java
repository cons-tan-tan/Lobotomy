package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class TheBurrowingHeavenEntity extends Abnormality {

    protected static final AnimationBuilder IDLE = new AnimationBuilder()
            .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);

    public TheBurrowingHeavenEntity(EntityType<? extends Abnormality> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setDefaultDamageType(DamageTypeUtil.BLACK);
        this.riskLevel = RiskLevelUtil.WAW;
        this.Defense = DefenseUtil.createDefense(0.0F, 1.2F, 0.5F, 1.5F);
    }

    public static AttributeSupplier setAttributes() {
        return Abnormality.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800.0F)
                .add(Attributes.ATTACK_DAMAGE, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).build();
    }

    private PlayState predicate(AnimationEvent event) {
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }
}

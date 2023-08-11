package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class JudgementBirdEntity extends AbnormalityEntity implements IAnimatable {

    private static final AnimationBuilder ANIM_ATTACK = new AnimationBuilder()
            .addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public JudgementBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "body_controller",
                0, this::predicate));
    }

    private PlayState predicate(AnimationEvent<JudgementBirdEntity> event) {
        if (this.swinging) {
            event.getController().setAnimation(ANIM_ATTACK);
        } else if (event.isMoving()) {
            event.getController().setAnimation(ANIM_WALK);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.1F).build();
    }
}

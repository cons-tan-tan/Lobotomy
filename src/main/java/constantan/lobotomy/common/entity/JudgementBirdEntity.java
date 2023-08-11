package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class JudgementBirdEntity extends AbnormalityEntity implements IAnimatable {

    public JudgementBirdEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.1F).build();
    }
}

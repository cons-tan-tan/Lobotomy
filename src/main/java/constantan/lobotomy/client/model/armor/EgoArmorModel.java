package constantan.lobotomy.client.model.armor;

import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EgoArmorModel<T extends Item & IAnimatable> extends AnimatedGeoModel<T> {

    protected final LibEntityResources.EntityResourceData entityResourceData;

    public EgoArmorModel(LibEntityResources.EntityResourceData entityResourceData) {
        super();
        this.entityResourceData = entityResourceData;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return entityResourceData.getArmorEgoModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return entityResourceData.getArmorEgoTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return entityResourceData.getArmorEgoAnimation();
    }

    public ResourceLocation getSuitTextureLocation(T object) {
        return entityResourceData.getSuitEgoTexture();
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        IBone rightArm = this.getAnimationProcessor().getBone("armorRightArm");
        IBone leftArm = this.getAnimationProcessor().getBone("armorLeftArm");

        rightArm.setScaleY(1.1F);
        rightArm.setScaleZ(1.1F);

        leftArm.setScaleY(1.1F);
        leftArm.setScaleZ(1.1F);


        if (animationEvent.getExtraDataOfType(LivingEntity.class).get(0) instanceof LivingEntity livingEntity) {
            if (livingEntity.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY) {
                IBone rightLeg = this.getAnimationProcessor().getBone("armorRightLeg");
                IBone leftLeg = this.getAnimationProcessor().getBone("armorLeftLeg");

                rightLeg.setHidden(true);
                leftLeg.setHidden(true);
            }
        }
    }
}

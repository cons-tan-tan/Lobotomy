package constantan.lobotomy.mixin;

import constantan.lobotomy.common.item.EgoRangeWeapon;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModel<T extends LivingEntity> {

    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart head;

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void poseRightArm(T pLivingEntity, CallbackInfo ci) {
        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;
        if (pLivingEntity instanceof Player player) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                ItemStack stack = player.getMainHandItem();
                Item item = stack.getItem();
                if (item instanceof EgoRangeWeapon) {
                    this.rightArm.xRot = this.head.xRot - ((float) Math.PI * 0.49F);
                    this.rightArm.yRot = this.head.yRot - 0.1F;
                    ci.cancel();
                }
            }
        }
    }
}

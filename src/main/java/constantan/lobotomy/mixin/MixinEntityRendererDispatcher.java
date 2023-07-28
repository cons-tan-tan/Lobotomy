package constantan.lobotomy.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.common.entity.PunishingBirdEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRendererDispatcher {
    @Inject(method = "renderHitbox", at = @At("HEAD"))
    private static void renderHitbox_Head(PoseStack pMatrixStack, VertexConsumer pBuffer, Entity pEntity, float pPartialTicks, CallbackInfo ci) {
        if (pEntity instanceof PunishingBirdEntity punishingBird) {
            AABB aabb = punishingBird.getAngryAttackAABB().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ());
            LevelRenderer.renderLineBox(pMatrixStack, pBuffer, aabb, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}

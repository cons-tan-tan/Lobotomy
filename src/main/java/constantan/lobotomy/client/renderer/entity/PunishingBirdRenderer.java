package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import constantan.lobotomy.client.model.entity.PunishingBirdModel;
import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;

public class PunishingBirdRenderer extends AbnormalityEntityRenderer<PunishingBirdEntity> {

    public PunishingBirdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PunishingBirdModel(LibEntityResources.PUNISHING_BIRD), 0.3F, 0.4F);
    }

//    @Override
//    public RenderType getRenderType(PunishingBirdEntity animatable, float partialTick, PoseStack poseStack,
//                                    @Nullable MultiBufferSource bufferSource,
//                                    @Nullable VertexConsumer buffer, int packedLight,
//                                    ResourceLocation texture) {
//        poseStack.scale(0.4F, 0.4F, 0.4F);
//
//        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
//    }

    @Override
    public void render(PunishingBirdEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        if (this.entityRenderDispatcher.shouldRenderHitBoxes() && !animatable.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            AABB aabb = animatable.getAngryAttackAABB(partialTick).move(-animatable.getX(), -animatable.getY(), -animatable.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}

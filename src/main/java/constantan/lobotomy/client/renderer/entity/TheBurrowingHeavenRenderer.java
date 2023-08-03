package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import constantan.lobotomy.client.model.entity.TheBurrowingHeavenModel;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;

public class TheBurrowingHeavenRenderer extends AbnormalityEntityRenderer<TheBurrowingHeavenEntity> {

    public TheBurrowingHeavenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TheBurrowingHeavenModel(LibEntityResources.THE_BURROWING_HEAVEN), 0.5F, 1.5F);
    }

    @Override
    public void render(TheBurrowingHeavenEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        if (this.entityRenderDispatcher.shouldRenderHitBoxes() && !animatable.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            AABB aabb = animatable.getBoundingBoxForCulling(partialTick).move(-animatable.getX(), -animatable.getY(), -animatable.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean shouldRender(TheBurrowingHeavenEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        boolean seen;

        if (!pLivingEntity.shouldRender(pCamX, pCamY, pCamZ)) {
            return false;
        } else if (pLivingEntity.noCulling) {
            return true;
        } else {
            AABB aabb = pLivingEntity.getBoundingBoxForCulling();

            seen = pCamera.isVisible(aabb);
        }

        pLivingEntity.clientShouldRenderer = seen;
        return seen;
    }
}

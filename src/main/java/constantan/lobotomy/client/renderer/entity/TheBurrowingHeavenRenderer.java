package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import constantan.lobotomy.client.model.entity.TheBurrowingHeavenModel;
import constantan.lobotomy.common.entity.custom.TheBurrowingHeaven;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class TheBurrowingHeavenRenderer extends AbnormalityRenderer<TheBurrowingHeaven> {

    public TheBurrowingHeavenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TheBurrowingHeavenModel(LibAbnormality.THE_BURROWING_HEAVEN), 0.5F, 1.5F);
    }

    @Override
    public void render(TheBurrowingHeaven animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        if (this.entityRenderDispatcher.shouldRenderHitBoxes() && !animatable.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            AABB aabb = animatable.getBoundingBoxForCulling(partialTick).move(-animatable.getX(), -animatable.getY(), -animatable.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean shouldRender(TheBurrowingHeaven pLivingEntity, @NotNull Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        boolean seen;

        if (!pLivingEntity.shouldRender(pCamX, pCamY, pCamZ)) {
            return false;
        } else if (pLivingEntity.noCulling) {
            return true;
        } else {
            AABB aabb = pLivingEntity.getBoundingBoxForCulling();

            seen = pCamera.isVisible(aabb);
        }

        pLivingEntity.clientShouldRender = seen;
        return seen;
    }
}

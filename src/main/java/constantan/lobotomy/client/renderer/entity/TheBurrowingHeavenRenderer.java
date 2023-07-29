package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.client.model.entity.TheBurrowingHeavenModel;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TheBurrowingHeavenRenderer extends GeoEntityRenderer<TheBurrowingHeavenEntity> {

    public TheBurrowingHeavenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TheBurrowingHeavenModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public RenderType getRenderType(TheBurrowingHeavenEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture) {
        poseStack.scale(1.5F, 1.5F, 1.5F);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }

    @Override
    public void render(TheBurrowingHeavenEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        if (this.entityRenderDispatcher.shouldRenderHitBoxes() && !animatable.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            AABB aabb = animatable.getBoundingBoxForCulling().move(-animatable.getX(), -animatable.getY(), -animatable.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean shouldRender(TheBurrowingHeavenEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        boolean seen = super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
        pLivingEntity.clientShouldRenderer = seen;
        return seen;
    }
}

package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.client.model.entity.PunishingBirdModel;
import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PunishingBirdRenderer extends GeoEntityRenderer<PunishingBirdEntity> {
    public PunishingBirdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PunishingBirdModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(PunishingBirdEntity animatable) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/entity/punishing_bird.png");
    }

    @Override
    public RenderType getRenderType(PunishingBirdEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture) {
        poseStack.scale(1.5f, 1.5f, 1.5f);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}

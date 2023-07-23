package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.client.model.entity.TheBurrowingHeavenModel;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
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
}

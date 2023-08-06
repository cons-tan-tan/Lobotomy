package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class AbnormalityEntityRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

    protected final float wholeScale;

    public AbnormalityEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model) {
        this(renderManager, model, 0.0F, 1.0F);
    }

    public AbnormalityEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model, float shadowRadius) {
        this(renderManager, model, shadowRadius, 1.0F);
    }

    public AbnormalityEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model, float shadowRadius, float scale) {
        super(renderManager, model);
        this.shadowRadius = shadowRadius;
        this.wholeScale = scale;
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture) {
        float s = this.wholeScale;
        poseStack.scale(s, s, s);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}

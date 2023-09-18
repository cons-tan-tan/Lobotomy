package constantan.lobotomy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.client.model.entity.AbnormalityModel;
import constantan.lobotomy.common.entity.custom.BigBird;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BigBirdRenderer extends AbnormalityRenderer<BigBird>{

    public BigBirdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbnormalityModel<>(LibAbnormality.BIG_BIRD), 1.0F, 1.5F);
    }

    @Override
    public RenderType getRenderType(BigBird animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}

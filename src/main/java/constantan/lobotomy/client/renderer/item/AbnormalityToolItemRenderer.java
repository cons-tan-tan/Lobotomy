package constantan.lobotomy.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.client.model.item.AbnormalityToolItemModel;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AbnormalityToolItemRenderer<T extends Item & IAnimatable> extends GeoItemRenderer<T> {

    public AbnormalityToolItemRenderer(LibAbnormality.ToolResourceData toolResourceData) {
        super(new AbnormalityToolItemModel<>(toolResourceData));
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.itemEntityTranslucentCull(texture);
    }
}

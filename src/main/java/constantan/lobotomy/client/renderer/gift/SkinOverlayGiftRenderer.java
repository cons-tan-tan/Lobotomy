package constantan.lobotomy.client.renderer.gift;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.client.model.gift.SkinOverlayModel;
import constantan.lobotomy.client.model.gift.EgoGiftModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SkinOverlayGiftRenderer implements ICurioRenderer {

    private final SkinOverlayModel<LivingEntity> model;
    private final ResourceLocation texture;

    public SkinOverlayGiftRenderer(ResourceLocation texture, EgoGiftModelPart part) {
        this.model = new SkinOverlayModel<>(part);
        this.texture = texture;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks, float netHeadYaw,
                                                                          float headPitch) {
        ICurioRenderer.followBodyRotations(slotContext.entity(), this.model);
        VertexConsumer buffer = renderTypeBuffer.getBuffer(this.model.renderType(this.texture));
        this.model.renderToBuffer(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}

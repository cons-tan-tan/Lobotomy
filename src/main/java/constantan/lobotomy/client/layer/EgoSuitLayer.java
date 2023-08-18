package constantan.lobotomy.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.common.item.EgoArmor;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EgoSuitLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final PlayerModel<T> model;

    public EgoSuitLayer(RenderLayerParent<T, M> pRenderer, PlayerModel<T> model) {
        super(pRenderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (shouldRender(pLivingEntity)) {
            this.getParentModel().copyPropertiesTo(model);

            this.model.head.visible = false;
            this.model.body.visible = false;
            this.model.leftArm.visible = false;
            this.model.rightArm.visible = false;
            this.model.leftLeg.visible = false;
            this.model.rightLeg.visible = false;

            HumanoidModel<?> parentPlayerModel = (HumanoidModel<?>) this.getParentModel();
            this.model.crouching = parentPlayerModel.crouching;
            this.model.rightArmPose = parentPlayerModel.rightArmPose;
            this.model.leftArmPose = parentPlayerModel.leftArmPose;

            this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(pLivingEntity)));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
        }
    }

    private static boolean shouldRender(LivingEntity livingEntity) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.getItem() instanceof EgoArmor egoArmor && egoArmor.hasSuitTexture()) {
            return true;
        }
        return false;
    }

    @Override
    protected ResourceLocation getTextureLocation(T pEntity) {
        if (pEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EgoArmor egoArmor) {
            return egoArmor.getSuitTexture();
        }
        return new ResourceLocation(LibMisc.MOD_ID, "textures/armor/heaven_suit.png");
    }
}

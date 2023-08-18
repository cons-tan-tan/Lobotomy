package constantan.lobotomy.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import constantan.lobotomy.common.item.EgoArmor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class EgoSuitLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final PlayerModel<T> model;

    public EgoSuitLayer(RenderLayerParent<T, M> pRenderer, PlayerModel<T> model) {
        super(pRenderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Item item = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem();
        if (item instanceof EgoArmor egoArmor && egoArmor.hasSuitTexture()) {
            this.getParentModel().copyPropertiesTo(model);

            this.model.head.visible = false;
            this.model.body.visible = false;
            this.model.leftArm.visible = false;
            this.model.rightArm.visible = false;
            this.model.leftLeg.visible = false;
            this.model.rightLeg.visible = false;

            if (egoArmor.hasInnerPart()) {
                for (SuitInnerPart part: egoArmor.getInnerPartSet()) {
                    this.setVisibility(part);
                }
            }

            HumanoidModel<?> parentPlayerModel = (HumanoidModel<?>) this.getParentModel();
            this.model.crouching = parentPlayerModel.crouching;
            this.model.rightArmPose = parentPlayerModel.rightArmPose;
            this.model.leftArmPose = parentPlayerModel.leftArmPose;

            this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(egoArmor.getSuitTexture()));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
        }
    }

    public void setVisibility(SuitInnerPart part) {
        switch (part) {
            case HEAD -> this.model.head.visible = true;
            case BODY -> this.model.body.visible = true;
            case RIGHT_ARM -> this.model.rightArm.visible = true;
            case LEFT_ARM -> this.model.leftArm.visible = true;
            case RIGHT_LEG -> this.model.rightLeg.visible = true;
            case LEFT_LEG -> this.model.leftLeg.visible = true;
        }
    }

    public enum SuitInnerPart {
        HEAD,
        BODY,
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_LEG,
        LEFT_LEG
    }
}

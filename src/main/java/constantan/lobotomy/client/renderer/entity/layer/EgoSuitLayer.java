package constantan.lobotomy.client.renderer.entity.layer;

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
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class EgoSuitLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final PlayerModel<T> model;

    public EgoSuitLayer(RenderLayerParent<T, M> pRenderer, PlayerModel<T> model) {
        super(pRenderer);
        this.model = model;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
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

            HumanoidModel<?> parentModel = (HumanoidModel<?>) this.getParentModel();
            if (pLivingEntity instanceof Player) {
                this.model.crouching = parentModel.crouching;
                this.model.rightArmPose = parentModel.rightArmPose;
                this.model.leftArmPose = parentModel.leftArmPose;

                this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
                this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

            } else if (pLivingEntity instanceof ArmorStand) {
                this.model.head.copyFrom(parentModel.head);
                this.model.body.copyFrom(parentModel.body);
                this.model.rightArm.copyFrom(parentModel.rightArm);
                this.model.leftArm.copyFrom(parentModel.leftArm);
                this.model.rightLeg.copyFrom(parentModel.rightLeg);
                this.model.leftLeg.copyFrom(parentModel.leftLeg);
                this.model.hat.copyFrom(parentModel.hat);
                this.model.jacket.copyFrom(this.model.body);
                this.model.rightSleeve.copyFrom(this.model.rightArm);
                this.model.leftSleeve.copyFrom(this.model.leftArm);
                this.model.rightPants.copyFrom(this.model.rightLeg);
                this.model.leftPants.copyFrom(this.model.leftLeg);
            }

            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(egoArmor.getSuitTexture(pLivingEntity)));
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

package constantan.lobotomy.client.model.gift;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class HeavenGiftModel<T extends Entity> extends AbstractGiftRenderer<T> {

    private final ModelPart head;

    public HeavenGiftModel() {
        ModelPart root = createBodyLayer().bakeRoot();
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition crown = head.addOrReplaceChild("crown", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.5F, -5.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition base = crown.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -31.0F, -5.5F, 5.0F, 0.5F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-5.0F, -30.5F, -5.5F, 3.0F, 0.5F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(8, 4).addBox(2.0F, -30.5F, -5.5F, 2.5F, 0.5F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(3, 9).addBox(4.5F, -30.0F, -4.5F, 0.5F, 0.5F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-5.0F, -30.0F, -5.5F, 0.5F, 0.5F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 30.0F, 5.0F));

        PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 2).addBox(-1.8F, -0.25F, -0.25F, 1.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6098F, -30.0157F, -5.25F, 0.0F, 0.0F, 0.6545F));

        PartDefinition cube_r2 = base.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 11).addBox(-3.3F, -0.25F, -0.25F, 4.0F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4397F, -31.2409F, -5.25F, 0.0F, 0.0F, 1.309F));

        PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(11, 9).addBox(-3.0F, -0.25F, -0.25F, 2.0F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0749F, -31.7856F, -5.25F, 0.0F, 0.0F, 1.309F));

        PartDefinition cube_r4 = base.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 9).addBox(-0.3F, -0.25F, -0.25F, 4.0F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.128F, -31.1187F, -5.25F, 0.0F, 0.0F, -1.0036F));

        PartDefinition eyes = crown.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.0F, 30.0F, 5.0F));

        PartDefinition cube_r5 = eyes.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 12).addBox(-0.75F, -0.75F, -0.75F, 1.5F, 1.5F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.55F, -32.25F, -5.25F, 0.0F, 0.0F, 0.2618F));

        PartDefinition eyes2 = crown.addOrReplaceChild("eyes2", CubeListBuilder.create(), PartPose.offset(0.0F, 32.0F, 5.0F));

        PartDefinition cube_r6 = eyes2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(11, 12).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.75F, -31.75F, -1.25F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r7 = eyes2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(11, 10).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -31.75F, -5.25F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public ModelPart[] getParts() {
        return new ModelPart[]{this.head};
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack,
                               @NotNull VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

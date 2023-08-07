package constantan.lobotomy.common.entity;

import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.common.network.packet.entity.TheBurrowingHeavenC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.ArrayList;
import java.util.List;

public class TheBurrowingHeavenEntity extends AbnormalityEntity implements IAnimatable {

    private static final AnimationBuilder ANIM_ACTIVATE = new AnimationBuilder()
            .addAnimation("activate", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);

    public static final float SEARCH_RANGE = 32.0F;

    public boolean clientShouldRender;
    private int clientCheckTick = 20;

    public boolean serverSeen = true;
    private int serverCheckTick = 20;
    private int subQliphothCounterSecond = 10;
    private int attackSecond = 3;

    public TheBurrowingHeavenEntity(EntityType<? extends AbnormalityEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    private <P extends Entity & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getQliphothCounter() == 0) {
            event.getController().setAnimation(ANIM_ACTIVATE);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800.0F)
                .add(Attributes.ATTACK_DAMAGE, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).build();
    }

    /**
     * 計算は{@link Entity#lookAt(EntityAnchorArgument.Anchor, Vec3)}を参考にした
     */
    public float getXRadForAnimation(float partialTick) {//弧度法
        Minecraft mc = Minecraft.getInstance();
        Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();
        double d0 = camera.x - this.getPosition(partialTick).x;
        double d1 = camera.y - (this.getPosition(partialTick).y + (this.getBoundingBox().getYsize() * 159 / 176));
        double d2 = camera.z - this.getPosition(partialTick).z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return (float) Mth.atan2(d1, d3);
    }

    /**
     * 同上
     */
    public float getYRadForAnimation(float partialTick) {//弧度法
        Minecraft mc = Minecraft.getInstance();
        Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();
        double d0 = camera.x - this.getPosition(partialTick).x;
        double d2 = camera.z - this.getPosition(partialTick).z;
        return  (float) ((Math.PI / 2) - Mth.atan2(d2, d0) + (this.getYRot() * (Math.PI / 180)));
    }

    public float getYRotForAnimation(float partialTick) {//度数法
        return (this.getYRadForAnimation(partialTick) * 180.0F / (float) Math.PI) - this.getYRot();
    }

    public AABB getBoundingBoxForCulling(float partialTick) {
        Vec3 pos = this.getPosition(1.0F);
        double x = 2.40625D * Math.cos(this.getYRotForAnimation(partialTick) * Math.PI / 180.0D);
        double z = 2.40625D * Math.sin(this.getYRotForAnimation(partialTick) * Math.PI / 180.0D);
        x = Math.max(Math.abs(x), this.getBoundingBox().getXsize() / 2);
        z = Math.max(Math.abs(z), this.getBoundingBox().getZsize() / 2);

        return new AABB(pos.x - x, pos.y, pos.z - z, pos.x + x, pos.y + 5.03125D, pos.z + z);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBoxForCulling(1.0F);
    }

    @Override
    public boolean canDoUnblockableAttack() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide && this.clientCheckTick-- == 0) {
            clientCheckTick = this.getQliphothCounter() == 0
                    ? 10
                    : 20;
            if (this.clientShouldRender) {
                Minecraft minecraft = Minecraft.getInstance();
                if (this.level == minecraft.player.level) {
                    Vec3 vec3 = this.getEyePosition();
                    Vec3 vec31 = minecraft.gameRenderer.getMainCamera().getPosition();
                    if (vec31.distanceTo(vec3) <= 128) {
                        if (this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS) {
                            Messages.sendToServer(new TheBurrowingHeavenC2SPacket(this.uuid));
                        }
                    }
                }
            }
        }

        if (!this.level.isClientSide) {
            if (this.serverCheckTick-- == 0) {//見られているかの判定
                this.serverCheckTick = 20;
                LobotomyMod.logger.info(this.serverSeen ? "seen" : "not seen");

                Vec3 pos = this.getPosition(1.0F);
                float r = SEARCH_RANGE;
                AABB searchArea = new AABB(pos.x - r, pos.y - r, pos.z - r, pos.x + r, pos.y + r, pos.z + r);

                List<Player> listPlayer = new ArrayList<>();
                for (Player player : this.level.getEntitiesOfClass(Player.class, searchArea)) {
                    if (this.hasLineOfSight(player)) {
                        listPlayer.add(player);
                    }
                }
                //地中の天国の視界内にプレイヤーがいる＆どのプレイヤーにも見られていない状態で10秒経つごとにクリフォトカウンターが1下がる
                if (!this.serverSeen) {
                    if (!listPlayer.isEmpty() && this.subQliphothCounterSecond-- == 0) {
                        this.subQliphothCounterSecond = 10;
                        this.subQliphothCounter(1);
                    }
                    if (this.getQliphothCounter() == 0 && this.attackSecond-- == 0) {
                        this.attackSecond = 3;
                        for (Player player : this.level.getEntitiesOfClass(Player.class, searchArea.inflate(SEARCH_RANGE))) {
                            this.doHurtTarget(player);
                        }
                    }
                } else {
                    this.subQliphothCounterSecond = 10;
                    this.attackSecond = 3;
                    this.serverSeen = false;
                }
            }
        }
    }
}

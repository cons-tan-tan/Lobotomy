package constantan.lobotomy.common.entity;

import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.network.Messages;
import constantan.lobotomy.common.network.packet.entity.TheBurrowingHeavenS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;

public class TheBurrowingHeavenEntity extends AbnormalityEntity implements IAnimatable {

    public static final float SEARCH_RANGE = 30.0F;

    protected static final AnimationBuilder IDLE = new AnimationBuilder()
            .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);

    public boolean clientShouldRenderer;

    public boolean serverSeen = true;
    private int serverCheckTick = 20;

    public TheBurrowingHeavenEntity(EntityType<? extends AbnormalityEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
        return AbnormalityEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800.0F)
                .add(Attributes.ATTACK_DAMAGE, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).build();
    }

    private <P extends Entity & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.serverCheckTick-- == 0) {
            this.serverCheckTick = 20;
            LobotomyMod.logger.info(this.serverSeen ? "seen" : "not seen");
            if (this.serverSeen) {

            }
            this.serverSeen = false;

            Vec3 pos = this.getPosition(1.0F);
            float r = TheBurrowingHeavenEntity.SEARCH_RANGE;
            AABB searchArea = new AABB(pos.x - r, pos.y - r, pos.z - r, pos.x + r, pos.y + r, pos.z + r);
            List<Player> listPlayer = this.level.getEntitiesOfClass(Player.class, searchArea);
            for (Player player : listPlayer) {
                if (this.getSensing().hasLineOfSight(player)) {
                    Messages.sendToPlayer(new TheBurrowingHeavenS2CPacket(this.getUUID(), player.getUUID()), (ServerPlayer) player);
                }
            }
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBoxForCulling(1.0F);
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
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    /**
     * 計算は{@link Entity#lookAt(EntityAnchorArgument.Anchor, Vec3)}を参考にした
     */
    public float getXRadForAnimation(float partialTick) {
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
    public float getYRadForAnimation(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();
        double d0 = camera.x - this.getPosition(partialTick).x;
        double d2 = camera.z - this.getPosition(partialTick).z;
        return  (float) ((Math.PI / 2) - Mth.atan2(d2, d0) + (this.getYRot() * (Math.PI / 180)));
    }

    public float getYRotForAnimation(float partialTick) {
        return (this.getYRadForAnimation(partialTick) * 180.0F / (float) Math.PI) - this.getYRot();
    }
}

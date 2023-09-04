package constantan.lobotomy.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class EgoUtil {

    public static boolean doConsumerToTargetInRange(Player player, float range, Consumer<Entity> entityConsumer) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 rangeVec = player.getViewVector(1.0F).normalize().scale(range);
        AABB searchArea = player.getBoundingBox().expandTowards(rangeVec).inflate(1.0F);
        EntityHitResult result = ProjectileUtil
                .getEntityHitResult(player, eyePos, eyePos.add(rangeVec), searchArea,
                        target -> !target.isSpectator() && target.isPickable(), Math.pow(range, 2));
        boolean flag = false;
        if (result != null) {
            Entity target = result.getEntity();
            Vec3 hitLocation = result.getLocation();
            flag = player.level
                    .clip(new ClipContext(eyePos, hitLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                    .getType() == HitResult.Type.MISS;
            if (flag) {
                entityConsumer.accept(target);
            }
        }
        return flag;
    }
}

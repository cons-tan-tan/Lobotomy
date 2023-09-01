package constantan.lobotomy.mixin;

import constantan.lobotomy.common.entity.CommonPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "pick", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;",
            shift = At.Shift.AFTER, ordinal = 5), locals = LocalCapture.CAPTURE_FAILHARD)
    private void pick_Inject(float pPartialTicks, CallbackInfo ci, Entity entity, double d0, Vec3 vec3, boolean flag, int i, double d1, double atkRange, Vec3 vec31, Vec3 vec32, float f, AABB aabb, EntityHitResult entityhitresult, Entity entity1, Vec3 vec33, double d2) {
        if (entity1 instanceof CommonPartEntity<?> partEntity) {
            minecraft.crosshairPickEntity = partEntity.getParent();
        }
    }
}

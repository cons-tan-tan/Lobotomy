package constantan.lobotomy.mixin;

import constantan.lobotomy.common.entity.CommonPartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class MixinPlayer {

    @ModifyVariable(method = "attack", at = @At("HEAD"), index = 1, argsOnly = true)
    private Entity attack_Head(Entity target) {
        if (target instanceof CommonPartEntity<?> partEntity) {
            return partEntity.getParent();
        }
        return target;
    }
}

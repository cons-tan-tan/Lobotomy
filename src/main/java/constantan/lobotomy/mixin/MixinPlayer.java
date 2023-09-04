package constantan.lobotomy.mixin;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;
import constantan.lobotomy.common.entity.CommonPartEntity;
import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Player.class)
public abstract class MixinPlayer implements IMixinPlayer {

    @Unique
    private EgoActionSequencer<?> egoActionSequencer = null;

    @ModifyVariable(method = "attack", at = @At("HEAD"), index = 1, argsOnly = true)
    private Entity attack_Head(Entity target) {
        if (target instanceof CommonPartEntity<?> partEntity) {
            return partEntity.getParent();
        }
        return target;
    }

    @Override
    public EgoActionSequencer<?> getEgoActionSequencer() {
        return this.egoActionSequencer;
    }

    @Override
    public void setEgoActionSequencer(EgoActionSequencer<?> egoActionSequencer) {
        if (this.egoActionSequencer == null) {
            this.egoActionSequencer = egoActionSequencer;
        }
    }

    @Override
    public boolean hasEgoActionSequencer() {
        return this.egoActionSequencer != null;
    }

    @Override
    public void removeEgoActionSequencer() {
        this.egoActionSequencer = null;
    }
}

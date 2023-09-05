package constantan.lobotomy.mixin;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;
import constantan.lobotomy.common.entity.CommonPartEntity;
import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Player.class)
public abstract class MixinPlayer implements IMixinPlayer {

    @Unique
    private final Map<EquipmentSlot, EgoActionSequencer<?>> egoActionSequencers = new HashMap<>();

    @ModifyVariable(method = "attack", at = @At("HEAD"), index = 1, argsOnly = true)
    private Entity attack_Head(Entity target) {
        if (target instanceof CommonPartEntity<?> partEntity) {
            return partEntity.getParent();
        }
        return target;
    }

    @Override
    public EgoActionSequencer<?> getEgoActionSequencer(EquipmentSlot equipmentSlot) {
        return this.egoActionSequencers.get(equipmentSlot);
    }

    @Override
    public void setEgoActionSequencer(EgoActionSequencer<?> egoActionSequencer) {
        this.egoActionSequencers.putIfAbsent(egoActionSequencer.getEquipmentSlot(), egoActionSequencer);
    }

    @Override
    public boolean hasEgoActionSequencer(EquipmentSlot equipmentSlot) {
        return this.egoActionSequencers.get(equipmentSlot) != null;
    }

    @Override
    public void removeEgoActionSequencer(EquipmentSlot equipmentSlot) {
        this.egoActionSequencers.put(equipmentSlot, null);
    }
}

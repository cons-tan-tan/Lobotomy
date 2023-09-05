package constantan.lobotomy.common.util.mixin;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;
import net.minecraft.world.entity.EquipmentSlot;

public interface IMixinPlayer {

    EgoActionSequencer<?> getEgoActionSequencer(EquipmentSlot equipmentSlot);

    void setEgoActionSequencer(EgoActionSequencer<?> egoActionSequencer);

    boolean hasEgoActionSequencer(EquipmentSlot equipmentSlot);

    void removeEgoActionSequencer(EquipmentSlot equipmentSlot);
}

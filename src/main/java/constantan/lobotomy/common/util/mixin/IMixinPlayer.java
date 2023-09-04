package constantan.lobotomy.common.util.mixin;

import constantan.lobotomy.common.ego.action.EgoActionSequencer;

public interface IMixinPlayer {

    EgoActionSequencer<?> getEgoActionSequencer();

    void setEgoActionSequencer(EgoActionSequencer<?> egoActionSequencer);

    boolean hasEgoActionSequencer();

    void removeEgoActionSequencer();
}

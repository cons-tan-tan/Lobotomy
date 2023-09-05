package constantan.lobotomy.common.ego.action;

import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class EgoActionSequencer<T> {

    private final EquipmentSlot equipmentSlot;
    private final ItemStack stack;
    private final T ego;
    private final Map<Integer, List<IEgoAction<T>>> timeLine;
    private final int tickLength;

    private int tick = 0;

    @SuppressWarnings("unchecked")
    public EgoActionSequencer(EquipmentSlot equipmentSlot, ItemStack stack, Map<Integer, List<IEgoAction<T>>> timeLine, int tickLength) {
        this.equipmentSlot = equipmentSlot;
        this.stack = stack;
        this.ego = (T) stack.getItem();
        this.timeLine = new HashMap<>(timeLine);
        this.tickLength = tickLength;
    }

    public void tick(Player player) {
        ItemStack currentStack = player.getItemBySlot(equipmentSlot);
        boolean flag = this.stack.equals(currentStack);

        if (flag) {
            this.tick++;

            if (timeLine.containsKey(this.tick)) {
                this.applyIfContainsInTimeLine(player, this.tick);
            }

            if (this.tick >= tickLength) {
                flag = false;
            }
        }

        if (!flag) {
            ((IMixinPlayer) player).removeEgoActionSequencer(this.equipmentSlot);
        }
    }

    public void applyIfContainsInTimeLine(Player player, int currentTick) {
        if (this.timeLine.containsKey(currentTick)) {
            for (IEgoAction<T> iEgoAction : timeLine.get(currentTick)) {
                iEgoAction.getAction().apply(player).apply(this.stack).apply(this.ego);
            }
        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    public boolean isInstant() {
        return this.tickLength == 0;
    }

    public void initAndSet(Player player) {
        this.applyIfContainsInTimeLine(player, 0);
        if (!this.isInstant()) {
            ((IMixinPlayer) player).setEgoActionSequencer(this);
        }
    }

    public static class Builder<T> {

        private final Map<Integer, List<IEgoAction<T>>> timeLine = new HashMap<>();
        private int tickLength = 0;

        public Builder<T> action(int tick, IEgoAction<T> iEgoAction) {
            return this.action(tick, List.of(iEgoAction));
        }

        public Builder<T> action(int tick, List<IEgoAction<T>> iEgoActions) {
            timeLine.computeIfAbsent(tick, key -> new ArrayList<>()).addAll(iEgoActions);
            this.tickLength = Math.max(this.tickLength, tick);
            return this;
        }

        public Builder<T> action(Set<Integer> tickSet, IEgoAction<T> iEgoAction) {
            for (int tick : tickSet) {
                this.action(tick, List.of(iEgoAction));
            }
            return this;
        }

        public Builder<T> instant(IEgoAction<T> iEgoAction) {
            return this.action(0, List.of(iEgoAction));
        }

        public Builder<T> instant(List<IEgoAction<T>> iEgoActions) {
            return this.action(0, iEgoActions);
        }

        public Builder<T> length(int tick) {
            this.tickLength = Math.max(this.tickLength, tick);
            return this;
        }

        public EgoActionSequencer<T> build(EquipmentSlot equipmentSlot, ItemStack stack) {
            return new EgoActionSequencer<>(equipmentSlot, stack, this.timeLine, this.tickLength);
        }
    }
}

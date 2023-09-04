package constantan.lobotomy.common.ego.action;

import constantan.lobotomy.common.util.mixin.IMixinPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class EgoActionSequencer<T> {

    private final ItemStack stack;
    private final T ego;
    private final Map<Integer, List<IEgoAction<T>>> timeLine;
    private final int tickLength;

    private int tick = 0;

    public EgoActionSequencer(ItemStack stack, Map<Integer, List<IEgoAction<T>>> timeLine, int tickLength) {
        this.stack = stack;
        this.ego = (T) stack.getItem();
        this.timeLine = new HashMap<>(timeLine);
        this.tickLength = tickLength;
    }

    public void tick(Player player) {
        this.tick++;

        if (timeLine.containsKey(this.tick)) {
            for (IEgoAction<T> iEgoAction : timeLine.get(this.tick)) {
                iEgoAction.getAction().apply(player).apply(this.stack).apply(this.ego);
            }
        }

        if (this.tick == tickLength) {
            ((IMixinPlayer) player).removeEgoActionSequencer();
        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public static class Builder<T> {

        private final Map<Integer, List<IEgoAction<T>>> timeLine = new HashMap<>();
        private int tickLength = 1;

        public Builder() {
        }

        public Builder<T> action(int tick, IEgoAction<T> iEgoAction) {
            timeLine.computeIfAbsent(tick, key -> new ArrayList<>()).add(iEgoAction);
            this.tickLength = Math.max(this.tickLength, tick);
            return this;
        }

        public Builder<T> action(int tick, List<IEgoAction<T>> iEgoActions) {
            timeLine.computeIfAbsent(tick, key -> new ArrayList<>()).addAll(iEgoActions);
            this.tickLength = Math.max(this.tickLength, tick);
            return this;
        }

        public Builder<T> action(Set<Integer> tickSet, IEgoAction<T> iEgoAction) {
            for (int tick : tickSet) {
                timeLine.computeIfAbsent(tick, key -> new ArrayList<>()).add(iEgoAction);
                this.tickLength = Math.max(this.tickLength, tick);
            }
            return this;
        }

        public Builder<T> length(int tick) {
            this.tickLength = Math.max(this.tickLength, tick);
            return this;
        }

        public EgoActionSequencer<T> build(ItemStack stack) {
            return new EgoActionSequencer<>(stack, this.timeLine, this.tickLength);
        }
    }
}

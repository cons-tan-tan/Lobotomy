package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;

public interface IAoEAttack {

    private Mob self() {
        return (Mob) this;
    }

    default void performAoEAttack(List<LivingEntity> list) {
        for (LivingEntity target : list) {
            self().doHurtTarget(target);
        }
    }
}

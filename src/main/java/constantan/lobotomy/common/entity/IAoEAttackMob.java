package constantan.lobotomy.common.entity;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface IAoEAttackMob {

    void performAoEAttack(List<LivingEntity> list);
}

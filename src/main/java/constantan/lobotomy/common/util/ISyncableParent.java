package constantan.lobotomy.common.util;

import constantan.lobotomy.common.util.IAnimatableParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.network.ISyncable;

public interface ISyncableParent extends IAnimatableParent {

    /**
     * {@link ISyncable}専用
     */
    void playAnimation(LivingEntity entity, ItemStack stack, int state);
}

package constantan.lobotomy.common.item;

import constantan.lobotomy.common.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Random;

public class GiantTreeSapItem extends AbnormalityToolItem {

    public static final int COMPENSATE_MAX_PERCENTAGE = 60;
    public static final int ADDED_COMPENSATE_PERCENTAGE = 15;

    public static int compensatePercentage = 0;

    public GiantTreeSapItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            player.heal(player.getMaxHealth());
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20*60, 4, false, false));
            int p = compensatePercentage;
            Random random = new Random();
            if (random.nextInt(100) < p) {
                player.addEffect(new MobEffectInstance(ModEffects.OWING.get(), 20*20, 0, false, false));
                compensatePercentage = 0;
            } else if (p < COMPENSATE_MAX_PERCENTAGE) {
                compensatePercentage = p + ADDED_COMPENSATE_PERCENTAGE;
            }
        }
        return itemStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }
}

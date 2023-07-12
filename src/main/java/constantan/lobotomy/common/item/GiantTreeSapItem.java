package constantan.lobotomy.common.item;

import constantan.lobotomy.common.init.ModEffects;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class GiantTreeSapItem extends ItemMod{
    private static final int DRINK_DURATION = 32;

    private static final int compensateMaxPercentage = 60;
    private static final int addedCompensatePercentage = 15;

    private static int compensatePercentage = 0;

    public GiantTreeSapItem(Properties properties) {
        super(properties);
    }

    public int getCompensatePercentage() {
        return compensatePercentage;
    }

    public void setCompensatePercentage(int p) {
        compensatePercentage = p;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("tooltip.lobotomy.giant_tree_sap.tooltip"));
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(new TranslatableComponent("tooltip.lobotomy.giant_tree_sap.tooltip.shift"));
        } else {
            pTooltipComponents.add(new TranslatableComponent("tooltip.lobotomy.press_shift.tooltip"));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20*60, 4));
            int p = this.getCompensatePercentage();
            Random random = new Random();
            if (random.nextInt(100) < p) {
                player.addEffect(new MobEffectInstance(ModEffects.OWING.get(), 20*20, 0, false, false));
                this.setCompensatePercentage(0);
            } else if (p < compensateMaxPercentage) {
                this.setCompensatePercentage(p + addedCompensatePercentage);
            }
        }
        return itemStack;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }
}

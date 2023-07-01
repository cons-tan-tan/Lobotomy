package constantan.lobotomy.common.item;

import constantan.lobotomy.common.effect.OwingEffect;
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

public class GiantTreeSap extends ItemMod{
    private static final int DRINK_DURATION = 32;

    private static float compensateProbability = 0f;
    public GiantTreeSap(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (Screen.hasControlDown()) {
            return new TranslatableComponent(this.getDescriptionId(pStack) + ".unknown");
        } else {
            return new TranslatableComponent(this.getDescriptionId(pStack));
        }
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
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 3));
            Random random = new Random();
            if (OwingEffect.getTargetUUID() != null) {
            } else if (random.nextFloat() < compensateProbability) {
                OwingEffect.setTargetUUID(player.getUUID());
                compensateProbability = 0F;
            } else if (compensateProbability < 0.6F) {
                compensateProbability += 0.15F;
            }
            if (player.getUUID() != OwingEffect.getTargetUUID()) {
                player.addEffect(new MobEffectInstance(ModEffects.OWING.get(), 400, 0, false, false, false)
                {
                    @Override
                    public int getDuration() {
                        return 0;
                    }
                });
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

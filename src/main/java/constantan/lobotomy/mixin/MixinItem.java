package constantan.lobotomy.mixin;

import constantan.lobotomy.common.util.IRiskLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class MixinItem {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void appendHoverText_Head(ItemStack pStack, Level pLevel,
                                      List<Component> pTooltipComponents, TooltipFlag pIsAdvanced, CallbackInfo ci) {
        var self = (Item) (Object) this;
        if (self instanceof IRiskLevel iRiskLevel) {
            pTooltipComponents.add(iRiskLevel.getRiskLevel().getColoredTextComponent());
        }
    }
}

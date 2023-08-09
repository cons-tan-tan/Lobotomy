package constantan.lobotomy.mixin;

import constantan.lobotomy.common.item.EgoMeleeWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;",
            shift = At.Shift.BEFORE), name = "list")
    private List<Component> getTooltipLines_Before_getValue(List<Component> list) {
        if (list.get(list.size() - 1).equals((new TranslatableComponent("item.modifiers." + EquipmentSlot.MAINHAND.getName())).withStyle(ChatFormatting.GRAY))
                && ((ItemStack) (Object) this).getItem() instanceof EgoMeleeWeapon egoMeleeWeapon) {
            list.add(new TextComponent(" ").append(egoMeleeWeapon.getDamageType().getColoredTextComponent().append(" (" + egoMeleeWeapon.getMinDamageAmount() + "-" + egoMeleeWeapon.getMaxDamageAmount() + ")").append(new TextComponent(" Attack Damage").withStyle(ChatFormatting.DARK_GREEN))));
        }
        return list;
    }
}

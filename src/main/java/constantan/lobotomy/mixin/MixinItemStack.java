package constantan.lobotomy.mixin;

import constantan.lobotomy.common.item.EgoMeleeWeapon;
import constantan.lobotomy.common.item.EgoRangeWeapon;
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
public abstract class MixinItemStack {

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;",
            shift = At.Shift.BEFORE), name = "list")
    private List<Component> getTooltipLines_Before_values(List<Component> list) {
        if (((ItemStack) (Object) this).getItem() instanceof EgoRangeWeapon egoRangeWeapon) {
            list.add(TextComponent.EMPTY);
            list.add(new TextComponent("When Hit Projectile:").withStyle(ChatFormatting.GRAY));
            list.add(new TextComponent(" ").append(egoRangeWeapon.getDamageType().getColoredTextComponent().append(" (" + egoRangeWeapon.getMinDamageAmount() + "-" + egoRangeWeapon.getMaxDamageAmount() + ")").append(new TextComponent(" Projectile Damage").withStyle(ChatFormatting.GOLD))));
        }
        return list;
    }

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

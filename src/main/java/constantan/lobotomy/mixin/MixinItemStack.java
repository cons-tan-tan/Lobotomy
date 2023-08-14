package constantan.lobotomy.mixin;

import constantan.lobotomy.common.item.EgoArmor;
import constantan.lobotomy.common.item.EgoMeleeWeapon;
import constantan.lobotomy.common.item.EgoRangeWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow public abstract Optional<TooltipComponent> getTooltipImage();

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;",
            shift = At.Shift.BEFORE), name = "list")
    private List<Component> getTooltipLines_Before_values(List<Component> list) {
        ItemStack itemStack = (ItemStack) (Object) this;
        Item item = itemStack.getItem();
        if (item instanceof EgoRangeWeapon egoRangeWeapon) {
            list.add(TextComponent.EMPTY);
            list.add(new TranslatableComponent("item.modifier.lobotomy.projectile").withStyle(ChatFormatting.GRAY));
            list.add(new TextComponent(" ").append(egoRangeWeapon.getAbnormalDamageTooltip(itemStack)));
        }
        return list;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;",
            shift = At.Shift.BEFORE), name = "list")
    private List<Component> getTooltipLines_Before_getValue(List<Component> list) {
        ItemStack itemStack = (ItemStack) (Object) this;
        Item item = itemStack.getItem();
        if (list.get(list.size() - 1).equals((new TranslatableComponent("item.modifiers." + EquipmentSlot.MAINHAND.getName())).withStyle(ChatFormatting.GRAY))) {
            if (item instanceof EgoMeleeWeapon egoMeleeWeapon) {
                list.add(new TextComponent(" ").append(egoMeleeWeapon.getAbnormalDamageTooltip(itemStack)));
            }
        } else if (list.get(list.size() - 1).equals((new TranslatableComponent("item.modifiers." + EquipmentSlot.CHEST.getName())).withStyle(ChatFormatting.GRAY))) {
            if (item instanceof EgoArmor egoArmor) {
                list.add(egoArmor.getDefenseMultiplierTooltip());
            }
        }
        return list;
    }
}

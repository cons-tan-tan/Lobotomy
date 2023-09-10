package constantan.lobotomy.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import constantan.lobotomy.common.item.EgoArmor;
import constantan.lobotomy.common.item.EgoMeleeWeapon;
import constantan.lobotomy.common.item.EgoRangeWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow public abstract Item getItem();

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;",
            shift = At.Shift.BEFORE), name = "list")
    private List<Component> getTooltipLines_Before_values(List<Component> list) {
        var self = (ItemStack) (Object) this;
        Item item = this.getItem();
        if (item instanceof EgoRangeWeapon egoRangeWeapon) {
            list.add(TextComponent.EMPTY);
            list.add(new TranslatableComponent("item.modifier.lobotomy.projectile").withStyle(ChatFormatting.GRAY));
            list.add(new TextComponent(" ").append(egoRangeWeapon.getAbnormalDamageTooltip(self)));
        }
        return list;
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 6), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipLines_Before_entries(Player pPlayer, TooltipFlag pIsAdvanced,
                                                CallbackInfoReturnable<List<Component>> cir,
                                                List<Component> list, MutableComponent mutablecomponent,
                                                int j, EquipmentSlot[] var6, int var7, int var8,
                                                EquipmentSlot equipmentslot,
                                                Multimap<Attribute, AttributeModifier> multimap) {
        var self = (ItemStack) (Object) this;
        Item item = this.getItem();
        if (item instanceof EgoMeleeWeapon egoMeleeWeapon) {
            list.add(new TextComponent(" ").append(egoMeleeWeapon.getAbnormalDamageTooltip(self)));
        } else if (item instanceof EgoArmor egoArmor) {
            list.add(egoArmor.getDefenseMultiplierTooltip());
        }
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 6), name = "multimap")
    private Multimap<Attribute, AttributeModifier> replaceMultimap(Multimap<Attribute, AttributeModifier> multimap) {
        if (this.getItem() instanceof EgoMeleeWeapon) {
            var nonAttackDamageMultiMap = HashMultimap.<Attribute, AttributeModifier>create();
            multimap.forEach((attribute, attributeModifier) -> {
                if (attribute != Attributes.ATTACK_DAMAGE) {
                    nonAttackDamageMultiMap.put(attribute, attributeModifier);
                }
            });
            return nonAttackDamageMultiMap;
        }
        return multimap;
    }
}

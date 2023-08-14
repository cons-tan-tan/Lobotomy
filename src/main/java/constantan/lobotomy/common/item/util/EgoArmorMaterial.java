package constantan.lobotomy.common.item.util;

import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class EgoArmorMaterial implements ArmorMaterial {

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private static final String NAME = "ego_armor_material_name";
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    public EgoArmorMaterial(RiskLevelUtil riskLevel, Map<DamageTypeUtil, Float> defense) {

        this.durabilityMultiplier = 5;
        this.slotProtections = new int[]{1, 1, riskLevel.getLevel() * 2, 1};
        this.enchantmentValue = 1;
        this.sound = SoundEvents.ARMOR_EQUIP_LEATHER;
        this.toughness = riskLevel.getLevel() - 1;
        this.knockbackResistance = riskLevel == RiskLevelUtil.ALEPH ? 0.2F : 0.0F;
        this.repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(ModItems.LOBOTOMY_DEBUG_ITEM.get()));
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot pSlot) {
        return this.slotProtections[pSlot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

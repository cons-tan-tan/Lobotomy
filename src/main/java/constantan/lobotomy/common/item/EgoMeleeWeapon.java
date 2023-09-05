package constantan.lobotomy.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import constantan.lobotomy.common.item.util.EgoMeleeWeaponType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.Lazy;

import java.util.UUID;
import java.util.function.Function;

public abstract class EgoMeleeWeapon extends EgoWeapon {

    private final EgoMeleeWeaponType weaponType;
    private final float range;
    private final float speed;
    public final boolean twoHanded;

    protected static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("4925e86b-3e02-417d-a35b-f7cfcb0750e1");

    private final Lazy<Function<EquipmentSlot, Function<ItemStack, Multimap<Attribute, AttributeModifier>>>> lazyDefaultModifiers;

    public EgoMeleeWeapon(int minDamage, int maxDamage, Properties pProperties) {
        super(minDamage, maxDamage, pProperties);

        var egoMeleeWeaponProperties = (EgoMeleeWeaponProperties) pProperties;
        this.weaponType = egoMeleeWeaponProperties.type;
        this.range = egoMeleeWeaponProperties.range;
        this.speed = egoMeleeWeaponProperties.speed;
        this.twoHanded = egoMeleeWeaponProperties.twoHanded;

        this.lazyDefaultModifiers = Lazy.of(() -> equipmentSlot -> itemstack -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.speed, AttributeModifier.Operation.ADDITION));
            if (this.range != 0) {
                builder.put(ForgeMod.ATTACK_RANGE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon modifier", this.range, AttributeModifier.Operation.ADDITION));
            }
            return builder.build();
        });
    }

    public EgoMeleeWeaponType getWeaponType() {
        return this.weaponType;
    }

    public float getAttackRange() {
        return range;
    }

    public float getAttackSpeed() {
        return speed;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND
                ? this.lazyDefaultModifiers.get().apply(slot).apply(stack)
                : super.getAttributeModifiers(slot, stack);
    }

    public static class EgoMeleeWeaponProperties extends EgoWeaponProperties<EgoMeleeWeaponProperties> {

        EgoMeleeWeaponType type;
        float range;
        float speed;
        boolean twoHanded;

        public EgoMeleeWeaponProperties weaponType(EgoMeleeWeaponType type) {
            this.type = type;
            this.range = type.getRange();
            this.speed = type.getSpeed();
            this.twoHanded = type.isTwoHanded();
            return this;
        }

        public EgoMeleeWeaponProperties range(float range) {
            this.range = range;
            return this;
        }

        public EgoMeleeWeaponProperties speed(float speed) {
            this.speed = speed;
            return this;
        }

        public EgoMeleeWeaponProperties isTwoHanded() {
            return this.isTwoHanded(true);
        }

        public EgoMeleeWeaponProperties isTwoHanded(boolean isTwoHanded) {
            this.twoHanded = isTwoHanded;
            return this;
        }
    }
}

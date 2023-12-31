package constantan.lobotomy.common.init;


import constantan.lobotomy.client.renderer.entity.layer.EgoSuitLayer;
import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.common.ego.weapon.EgoMeleeWeaponType;
import constantan.lobotomy.common.item.*;
import constantan.lobotomy.common.item.abnormality.GiantTreeSapItem;
import constantan.lobotomy.common.item.ego.SimpleEgoArmorItem;
import constantan.lobotomy.common.item.ego.SimpleEgoMeleeWeapon;
import constantan.lobotomy.common.item.ego.SimpleEgoRangeWeapon;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.lib.LibAbnormality;
import constantan.lobotomy.lib.LibItemNames;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.MOD_ID);

    private static Item.Properties basicItem() {
        return new Item.Properties().tab(ModSetup.CREATIVE_TAB);
    }

    private static AbnormalityTool.AbnormalityToolItemProperties tool() {
        return new AbnormalityTool.AbnormalityToolItemProperties();
    }

    private static EgoMeleeWeapon.EgoMeleeWeaponProperties meleeWeapon() {
        return new EgoMeleeWeapon.EgoMeleeWeaponProperties();
    }

    private static EgoRangeWeapon.EgoRangeWeaponProperties rangeWeapon() {
        return new EgoRangeWeapon.EgoRangeWeaponProperties();
    }

    private static EgoArmor.EgoArmorProperties armor() {
        return new EgoArmor.EgoArmorProperties();
    }

    private static Item.Properties gift() {
        return new Item.Properties();
    }

    //DEBUG
    public static final RegistryObject<Item> LOBOTOMY_DEBUG_ITEM = ITEMS.register(LibItemNames.LOBOTOMY_DEBUG_ITEM,
            () -> new LobotomyDebugItem(basicItem().stacksTo(1)));

    //ABNORMALITY_SPAWN_EGG
    public static final RegistryObject<Item> PUNISHING_BIRD_SPAWN_EGG = ITEMS.register(LibAbnormality.PUNISHING_BIRD.getSpawnEggName(),
            () -> new AbnormalitySpawnEggItem(ModEntityTypes.PUNISHING_BIRD, 0xffffff, 0xadc5f0));

    public static final RegistryObject<Item> JUDGEMENT_BIRD_SPAWN_EGG = ITEMS.register(LibAbnormality.JUDGEMENT_BIRD.getSpawnEggName(),
            () -> new AbnormalitySpawnEggItem(ModEntityTypes.JUDGEMENT_BIRD, 0x222222, 0xe8e8e8));

    public static final RegistryObject<Item> THE_BURROWING_HEAVEN_SPAWN_EGG = ITEMS.register(LibAbnormality.THE_BURROWING_HEAVEN.getSpawnEggName(),
            () -> new AbnormalitySpawnEggItem(ModEntityTypes.THE_BURROWING_HEAVEN, 0xb40a1a, 0xe4af50));

    public static final RegistryObject<Item> BIG_BIRD_SPAWN_EGG = ITEMS.register(LibAbnormality.BIG_BIRD.getSpawnEggName(),
            () -> new AbnormalitySpawnEggItem(ModEntityTypes.BIG_BIRD, 0x000000, 0xf5c62b));

    //ABNORMALITY_TOOL
    public static final RegistryObject<Item> GIANT_TREE_SAP = ITEMS.register(LibAbnormality.GIANT_TREE_SAP.name(),
            () -> new GiantTreeSapItem(tool().riskLevel(RiskLevelUtil.HE)));

    //EGO_WEAPON
    public static final RegistryObject<Item> PEAK_WEAPON = ITEMS.register(LibAbnormality.PUNISHING_BIRD.getWeaponEgoName(),
            () -> new SimpleEgoRangeWeapon(2, 3, rangeWeapon()
                    .damageType(DamageTypeUtil.RED)
                    .riskLevel(RiskLevelUtil.TETH)));

    public static final RegistryObject<Item> JUSTITIA_WEAPON = ITEMS.register(LibAbnormality.JUDGEMENT_BIRD.getWeaponEgoName(),
            () -> new SimpleEgoMeleeWeapon(2, 4, meleeWeapon()
                    .weaponType(EgoMeleeWeaponType.SWORD)
                    .damageType(DamageTypeUtil.PALE)
                    .riskLevel(RiskLevelUtil.ALEPH)
                    .afterAttackAction(ModActions.JUSTITIA_ATTACK_NORMAL)));

    public static final RegistryObject<Item> HEAVEN_WEAPON = ITEMS.register(LibAbnormality.THE_BURROWING_HEAVEN.getWeaponEgoName(),
            () -> new SimpleEgoMeleeWeapon(8, 16, meleeWeapon()
                    .weaponType(EgoMeleeWeaponType.SPEAR)
                    .damageType(DamageTypeUtil.RED)
                    .riskLevel(RiskLevelUtil.WAW)));

    //EGO_ARMOR
    public static final RegistryObject<Item> PEAK_ARMOR = ITEMS.register(LibAbnormality.PUNISHING_BIRD.getArmorEgoName(),
            () -> new SimpleEgoArmorItem(armor()
                    .suitInnerPart(EgoSuitLayer.SuitInnerPart.LEFT_ARM)
                    .defense(0.7F, 0.8F, 1.2F, 2.0F)
                    .riskLevel(RiskLevelUtil.TETH)));

    public static final RegistryObject<Item> JUSTITIA_ARMOR = ITEMS.register(LibAbnormality.JUDGEMENT_BIRD.getArmorEgoName(),
            () -> new SimpleEgoArmorItem(armor()
                    .defense(0.5F, 0.5F, 0.5F, 0.5F)
                    .riskLevel(RiskLevelUtil.ALEPH)));

    public static final RegistryObject<Item> HEAVEN_ARMOR = ITEMS.register(LibAbnormality.THE_BURROWING_HEAVEN.getArmorEgoName(),
            () -> new SimpleEgoArmorItem(armor()
                    .defense(1.2F,0.8F,0.6F,1.5F)
                    .riskLevel(RiskLevelUtil.WAW)
                    .idleAnim()));

    //EGO_GIFT
    public static final RegistryObject<Item> JUSTITIA_GIFT = ITEMS.register(LibAbnormality.JUDGEMENT_BIRD.getGiftEgoName(),
            () -> new EgoGift(gift()));

    public static final RegistryObject<Item> HEAVEN_GIFT = ITEMS.register(LibAbnormality.THE_BURROWING_HEAVEN.getGiftEgoName(),
            () -> new EgoGift(gift()));
}

package constantan.lobotomy.common.init;


import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.common.item.AbnormalityToolItem;
import constantan.lobotomy.common.item.GiantTreeSapItem;
import constantan.lobotomy.common.item.LobotomyDebugItem;
import constantan.lobotomy.common.item.PeakWeaponItem;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.lib.LibBlockNames;
import constantan.lobotomy.lib.LibEntityResources;
import constantan.lobotomy.lib.LibItemNames;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.MOD_ID);

    public static Item.Properties basicItem() {
        return new Item.Properties().tab(ModSetup.CREATIVE_TAB);
    }

    public static Item.Properties abnormalityToolItem(RiskLevelUtil riskLevel) {
        return new AbnormalityToolItem.AbnormalityToolItemProperties().riskLevel(riskLevel).tab(ModSetup.CREATIVE_TAB).stacksTo(1);
    }

    //DEBUG
    public static final RegistryObject<Item> LOBOTOMY_DEBUG_ITEM = ITEMS.register("lobotomy_debug_item",
            () -> new LobotomyDebugItem(basicItem().stacksTo(1)));

    public static final RegistryObject<Item> FACTORY_BLOCK = ITEMS.register(LibBlockNames.FACTORY_BLOCK,
            () -> new BlockItem(ModBlocks.FACTORY_BLOCK.get(), basicItem()));

    //ABNORMALITY_TOOL
    public static final RegistryObject<Item> GIANT_TREE_SAP = ITEMS.register(LibItemNames.GIANT_TREE_SAP,
            () -> new GiantTreeSapItem(abnormalityToolItem(RiskLevelUtil.HE).food((new FoodProperties.Builder()).alwaysEat().build())));

    //ABNORMALITY_SPAWN_EGG
    public static final RegistryObject<Item> PUNISHING_BIRD_SPAWN_EGG = ITEMS.register(LibEntityResources.PUNISHING_BIRD.getSpawnEggName(),
            () -> new ForgeSpawnEggItem(ModEntityTypes.PUNISHING_BIRD, 0xffffff, 0xadc5f0, basicItem()));

    public static final RegistryObject<Item> THE_BURROWING_HEAVEN_SPAWN_EGG = ITEMS.register(LibEntityResources.THE_BURROWING_HEAVEN.getSpawnEggName(),
            () -> new ForgeSpawnEggItem(ModEntityTypes.THE_BURROWING_HEAVEN, 0xb40a1a, 0xe4af50, basicItem()));

    //EGO_WEAPON
    public static final RegistryObject<Item> PEAK_WEAPON_ITEM = ITEMS.register(LibItemNames.PEAK_WEAPON,
            () -> new PeakWeaponItem(basicItem().stacksTo(1)));
}

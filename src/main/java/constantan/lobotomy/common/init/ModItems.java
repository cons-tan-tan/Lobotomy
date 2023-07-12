package constantan.lobotomy.common.init;


import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.item.GiantTreeSapItem;
import constantan.lobotomy.common.item.PeakWeaponItem;
import constantan.lobotomy.lib.LibBlockNames;
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
        return new Item.Properties().tab(LobotomyMod.CREATIVE_TAB);
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return new Item.Properties().food(food).tab(LobotomyMod.CREATIVE_TAB);
    }

    public static final RegistryObject<Item> FACTORY_BLOCK = ITEMS.register(LibBlockNames.FACTORY_BLOCK,
            () -> new BlockItem(ModBlocks.FACTORY_BLOCK.get(), basicItem()));

    public static final RegistryObject<Item> GIANT_TREE_SAP = ITEMS.register(LibItemNames.GIANT_TREE_SAP,
            () -> new GiantTreeSapItem(foodItem(ModFoods.GIANT_TREE_SAP).stacksTo(1)));

    public static final RegistryObject<Item> PUNISHING_BIRD_SPAWN_EGG = ITEMS.register(LibItemNames.PUNISHING_BIRD_SPAWN_EGG,
            () -> new ForgeSpawnEggItem(ModEntityTypes.PUNISHING_BIRD, 0xffffff, 0xadc5f0, basicItem()));

    public static final RegistryObject<Item> PEAK_WEAPON_ITEM = ITEMS.register(LibItemNames.PEAK_WEAPON,
            () -> new PeakWeaponItem(basicItem().stacksTo(1)));
}

package constantan.lobotomy.datagen;

import constantan.lobotomy.common.init.ModItems;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.SeparatePerspectiveModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.BiFunction;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(ModItems.PUNISHING_BIRD_SPAWN_EGG.get());
        spawnEggItem(ModItems.JUDGEMENT_BIRD_SPAWN_EGG.get());
        spawnEggItem(ModItems.THE_BURROWING_HEAVEN_SPAWN_EGG.get());

        armorItem(ModItems.PEAK_ARMOR.get());
        armorItem(ModItems.JUSTITIA_ARMOR.get());
        armorItem(ModItems.HEAVEN_ARMOR.get());

        multiModelItem(ModItems.PEAK_WEAPON.get());
        multiModelItem(ModItems.JUSTITIA_WEAPON.get());
        multiModelItem(ModItems.HEAVEN_WEAPON.get());

        multiModelItem(ModItems.GIANT_TREE_SAP.get());
    }

    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LibMisc.MOD_ID, "item/" + item.getRegistryName().getPath()));
    }

    private ItemModelBuilder spawnEggItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/template_spawn_egg"));
    }

    private ItemModelBuilder armorItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LibMisc.MOD_ID, "item/" + item.getRegistryName().getPath() + "_gui"));
    }

    private SeparatePerspectiveModelBuilder<ItemModelBuilder> multiModelItem(Item item) {

        BiFunction<ItemModelBuilder, ExistingFileHelper, ? extends CustomLoaderBuilder<ItemModelBuilder>> BiFunction = new BiFunction<>() {
            @Override
            public CustomLoaderBuilder<ItemModelBuilder> apply(ItemModelBuilder itemModelBuilder, ExistingFileHelper existingFileHelper) {
                return SeparatePerspectiveModelBuilder.begin(itemModelBuilder, existingFileHelper)
                        .base(multiModelInHandItem(item))
                        .perspective(ItemTransforms.TransformType.GUI, multiModelGuiItem(item));
            }
        };

        return (SeparatePerspectiveModelBuilder<ItemModelBuilder>) withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("forge:item/default"))
                .customLoader(BiFunction);
    }

    private ItemModelBuilder multiModelInHandItem(Item item) {
        return withExistingParent("1" + item.toString(), new ResourceLocation(LibMisc.MOD_ID, "item/" + item.toString() + "_in_hand"));
    }

    private ItemModelBuilder multiModelGuiItem(Item item) {
        return withExistingParent("0" + item.toString(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LibMisc.MOD_ID, "item/" + item.getRegistryName().getPath() + "_gui"));
    }
}

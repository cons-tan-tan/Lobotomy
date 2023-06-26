package constantan.lobotomy.common.init;

import constantan.lobotomy.lib.LibBlockNames;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LibMisc.MOD_ID);

    public static final RegistryObject<Block> FACTORY_BLOCK = BLOCKS.register(LibBlockNames.FACTORY_BLOCK,
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.FARMLAND)));

}

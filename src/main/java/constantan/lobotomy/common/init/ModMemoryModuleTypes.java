package constantan.lobotomy.common.init;

import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public class ModMemoryModuleTypes {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, LibMisc.MOD_ID);

    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> IN_AOE_LIVING_ENTITY =
            MEMORY_MODULE_TYPES.register("in_aoe_living_entity", () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> IN_CHECK_AREA_LIVING_ENTITY =
            MEMORY_MODULE_TYPES.register("in_check_area_living_entity", () -> new MemoryModuleType<>(Optional.empty()));
}

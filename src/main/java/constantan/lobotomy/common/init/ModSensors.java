package constantan.lobotomy.common.init;

import constantan.lobotomy.common.entity.ai.sensor.LivingEntityInAoESensor;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSensors {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, LibMisc.MOD_ID);

    public static final RegistryObject<SensorType<LivingEntityInAoESensor<?>>> LIVING_ENTITY_IN_AOE = SENSOR_TYPES.register("living_entity_in_aoe",
            () -> new SensorType<>(LivingEntityInAoESensor::new));
}

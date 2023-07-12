package constantan.lobotomy.common.init;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibEntityResources;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, LibMisc.MOD_ID);

    public static final RegistryObject<EntityType<PunishingBirdEntity>> PUNISHING_BIRD = ENTITY_TYPES.register(LibEntityResources.PUNISHING_BIRD_NAME,
            () -> EntityType.Builder.of(PunishingBirdEntity::new, MobCategory.MONSTER)
                    .sized(0.4f, 0.65f)
                    .build(LibEntityResources.PUNISHING_BIRD_BUILD));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

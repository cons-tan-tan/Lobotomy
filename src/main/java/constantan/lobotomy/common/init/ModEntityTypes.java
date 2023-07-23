package constantan.lobotomy.common.init;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.lib.LibEntityResources;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, LibMisc.MOD_ID);

    public static final RegistryObject<EntityType<PunishingBirdEntity>> PUNISHING_BIRD = ENTITY_TYPES.register(LibEntityResources.PUNISHING_BIRD.getName(),
            () -> EntityType.Builder.of(PunishingBirdEntity::new, MobCategory.MONSTER)
                    .sized(0.4F, 0.65F)
                    .build(LibEntityResources.PUNISHING_BIRD.getBuild()));

    public static final RegistryObject<EntityType<TheBurrowingHeavenEntity>> THE_BURROWING_HEAVEN = ENTITY_TYPES.register(LibEntityResources.THE_BURROWING_HEAVEN.getName(),
            () -> EntityType.Builder.of(TheBurrowingHeavenEntity::new, MobCategory.MONSTER)
                    .sized(0.4F, 2.75F)
                    .build(LibEntityResources.THE_BURROWING_HEAVEN.getBuild()));


    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdEntity.setAttributes());
        event.put(ModEntityTypes.THE_BURROWING_HEAVEN.get(), TheBurrowingHeavenEntity.setAttributes());
    }
}

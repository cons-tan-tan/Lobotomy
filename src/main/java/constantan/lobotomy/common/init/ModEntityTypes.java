package constantan.lobotomy.common.init;

import constantan.lobotomy.common.entity.AbnormalityEntity.AbnormalityEntityTypeProperties;
import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.common.entity.TheBurrowingHeavenEntity;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.lib.LibEntityResources;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, LibMisc.MOD_ID);

    public static final RegistryObject<EntityType<PunishingBirdEntity>> PUNISHING_BIRD = ENTITY_TYPES.register(LibEntityResources.PUNISHING_BIRD.getName(),
            () -> AbnormalityEntityTypeProperties.Builder
                    .of(PunishingBirdEntity::new, MobCategory.MONSTER)
                    .sized(0.4F, 0.65F)
                    .build(LibEntityResources.PUNISHING_BIRD.getBuild())
                    .riskLevel(RiskLevelUtil.TETH)
                    .damageType(DamageTypeUtil.RED)
                    .defense(2.0F, 2.0F, 2.0F, 2.0F));

    public static final RegistryObject<EntityType<TheBurrowingHeavenEntity>> THE_BURROWING_HEAVEN = ENTITY_TYPES.register(LibEntityResources.THE_BURROWING_HEAVEN.getName(),
            () -> AbnormalityEntityTypeProperties.Builder
                    .of(TheBurrowingHeavenEntity::new, MobCategory.MONSTER)
                    .sized(0.4F, 2.75F)
                    .build(LibEntityResources.THE_BURROWING_HEAVEN.getBuild())
                    .riskLevel(RiskLevelUtil.WAW)
                    .damageType(DamageTypeUtil.BLACK)
                    .defense(0.0F, 1.2F, 0.5F, 1.5F));


    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBirdEntity.setAttributes());
        event.put(ModEntityTypes.THE_BURROWING_HEAVEN.get(), TheBurrowingHeavenEntity.setAttributes());
    }
}

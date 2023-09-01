package constantan.lobotomy.common.init;

import constantan.lobotomy.common.entity.custom.JudgementBird;
import constantan.lobotomy.common.entity.custom.PunishingBird;
import constantan.lobotomy.common.entity.custom.TheBurrowingHeaven;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import constantan.lobotomy.lib.LibAbnormality;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, LibMisc.MOD_ID);

    public static final RegistryObject<EntityType<PunishingBird>> PUNISHING_BIRD = ENTITY_TYPES
            .register(LibAbnormality.PUNISHING_BIRD.getName(),
                    () -> abnormalityEntityType(EntityType.Builder
                            .of(PunishingBird::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.65F)
                            .build(LibAbnormality.PUNISHING_BIRD.getBuild()))
                            .riskLevel(RiskLevelUtil.TETH)
                            .damageType(DamageTypeUtil.RED)
                            .defense(2.0F, 2.0F, 2.0F, 2.0F)
                            .qliphothCounter(4)
                            .build());

    public static final RegistryObject<EntityType<JudgementBird>> JUDGEMENT_BIRD = ENTITY_TYPES
            .register(LibAbnormality.JUDGEMENT_BIRD.getName(),
                    () -> abnormalityEntityType(EntityType.Builder
                            .of(JudgementBird::new, MobCategory.MONSTER)
                            .sized(0.9F, 3.2F)
                            .build(LibAbnormality.JUDGEMENT_BIRD.getBuild()))
                            .riskLevel(RiskLevelUtil.WAW)
                            .damageType(DamageTypeUtil.PALE)
                            .defense(0.8F, 0.8F, 0.8F, 2.0F)
                            .qliphothCounter(2)
                            .noKnockbackAttacker()
                            .unblockableAttacker()
                            .build());

    public static final RegistryObject<EntityType<TheBurrowingHeaven>> THE_BURROWING_HEAVEN = ENTITY_TYPES
            .register(LibAbnormality.THE_BURROWING_HEAVEN.getName(),
                    () -> abnormalityEntityType(EntityType.Builder
                            .of(TheBurrowingHeaven::new, MobCategory.MONSTER)
                            .sized(0.6F, 2.75F)
                            .build(LibAbnormality.THE_BURROWING_HEAVEN.getBuild()))
                            .riskLevel(RiskLevelUtil.WAW)
                            .damageType(DamageTypeUtil.BLACK)
                            .defense(0.0F, 1.2F, 0.5F, 1.5F)
                            .qliphothCounter(3)
                            .noKnockbackAttacker()
                            .unblockableAttacker()
                            .build());


    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PUNISHING_BIRD.get(), PunishingBird.setAttributes());
        event.put(ModEntityTypes.THE_BURROWING_HEAVEN.get(), TheBurrowingHeaven.setAttributes());
        event.put(ModEntityTypes.JUDGEMENT_BIRD.get(), JudgementBird.setAttributes());
    }


    @SuppressWarnings("unchecked")
    public static <T extends Entity> IMixinEntityType<T> abnormalityEntityType(EntityType<T> entityType) {
        return (IMixinEntityType<T>) entityType;
    }
}

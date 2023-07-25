package constantan.lobotomy.common.entity;

import com.google.common.collect.ImmutableSet;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public abstract class AbnormalityEntity extends Monster implements IRiskLevel, IAnimatable {

    public final AnimationFactory FACTORY;

    public final RiskLevelUtil riskLevel;
    public final DamageTypeUtil defaultDamageType;
    public final Map<DamageTypeUtil, Float> defense;

    public DamageTypeUtil currentDamageType;

    public AbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.FACTORY = GeckoLibUtil.createFactory(this);

        AbnormalityEntityTypeProperties<? extends Monster> abnormalityEntityTypeProperties = (AbnormalityEntityTypeProperties<? extends Monster>) pEntityType;
        this.riskLevel = abnormalityEntityTypeProperties.riskLevel;
        this.defaultDamageType = abnormalityEntityTypeProperties.defaultDamageType;
        this.defense = abnormalityEntityTypeProperties.defense;

        this.currentDamageType = this.defaultDamageType;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        DamageTypeUtil damageType = this.getCurrentDamageType();
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (pEntity instanceof LivingEntity livingEntity) {
            return damageType.hurtLivingEntity(livingEntity, this, f);
        }

        return super.doHurtTarget(pEntity);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    public Map<DamageTypeUtil, Float> getDefense() {
        return this.defense;
    }

    public DamageTypeUtil getDefaultDamageType() {
        return this.defaultDamageType;
    }

    public DamageTypeUtil getCurrentDamageType() {
        if (this.currentDamageType != null) {
            return this.currentDamageType;
        }
        return this.defaultDamageType;
    }

    public void setCurrentDamageType(DamageTypeUtil damageType) {
        this.currentDamageType = damageType;
    }

    public void resetCurrentDamageType() {
        this.currentDamageType = this.defaultDamageType;
    }


    public static class AbnormalityEntityTypeProperties<T extends Entity> extends EntityType<T>{

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
        DamageTypeUtil defaultDamageType = DamageTypeUtil.RED;
        Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

        public AbnormalityEntityTypeProperties(EntityFactory<T> pFactory, MobCategory pCategory, boolean pSerialize, boolean pSummon, boolean pFireImmune, boolean pCanSpawnFarFromPlayer, ImmutableSet<Block> pImmuneTo, EntityDimensions pDimensions, int pClientTrackingRange, int pUpdateInterval, Predicate<EntityType<?>> velocityUpdateSupplier, ToIntFunction<EntityType<?>> trackingRangeSupplier, ToIntFunction<EntityType<?>> updateIntervalSupplier, BiFunction<PlayMessages.SpawnEntity, Level, T> customClientFactory) {
            super(pFactory, pCategory, pSerialize, pSummon, pFireImmune, pCanSpawnFarFromPlayer, pImmuneTo, pDimensions, pClientTrackingRange, pUpdateInterval, velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
        }

        public static <E extends Entity> AbnormalityEntityTypeProperties<E> cast(EntityType<E> entityType) {
            return (AbnormalityEntityTypeProperties<E>) entityType;
        }

        public AbnormalityEntityTypeProperties<T> riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public AbnormalityEntityTypeProperties<T> damageType(DamageTypeUtil defaultDamageType) {
            this.defaultDamageType = defaultDamageType;
            return this;
        }

        public AbnormalityEntityTypeProperties<T> defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }

        public static class Builder<T extends Entity> extends EntityType.Builder<T>{

            public Builder(EntityFactory pFactory, MobCategory pCategory) {
                super(pFactory, pCategory);
            }

            //コピペ
            public static <T extends Entity> AbnormalityEntityTypeProperties.Builder<T> of(EntityType.EntityFactory<T> pFactory, MobCategory pCategory) {
                return new AbnormalityEntityTypeProperties.Builder<>(pFactory, pCategory);
            }

            //コピペ
            public static <T extends Entity> AbnormalityEntityTypeProperties.Builder<T> createNothing(MobCategory pCategory) {
                return new AbnormalityEntityTypeProperties.Builder<>((p_20708_, p_20709_) -> {
                    return (T)null;
                }, pCategory);
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> sized(float pWidth, float pHeight) {
                this.dimensions = EntityDimensions.scalable(pWidth, pHeight);
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> noSummon() {
                this.summon = false;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> noSave() {
                this.serialize = false;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> fireImmune() {
                this.fireImmune = true;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> immuneTo(Block... pBlocks) {
                this.immuneTo = ImmutableSet.copyOf(pBlocks);
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> canSpawnFarFromPlayer() {
                this.canSpawnFarFromPlayer = true;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> clientTrackingRange(int pClientTrackingRange) {
                this.clientTrackingRange = pClientTrackingRange;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> updateInterval(int pUpdateInterval) {
                this.updateInterval = pUpdateInterval;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> setUpdateInterval(int interval) {
                this.updateIntervalSupplier = t->interval;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> setTrackingRange(int range) {
                this.trackingRangeSupplier = t->range;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> setShouldReceiveVelocityUpdates(boolean value) {
                this.velocityUpdateSupplier = t->value;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties.Builder<T> setCustomClientFactory(java.util.function.BiFunction<net.minecraftforge.network.PlayMessages.SpawnEntity, Level, T> customClientFactory) {
                this.customClientFactory = customClientFactory;
                return this;
            }

            @Override
            public AbnormalityEntityTypeProperties<T> build(String pKey) {
                if (this.serialize) {
                    Util.fetchChoiceType(References.ENTITY_TREE, pKey);
                }

                return new AbnormalityEntityTypeProperties<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval, velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
            }
        }
    }
}

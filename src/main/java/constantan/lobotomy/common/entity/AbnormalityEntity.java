package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.util.*;
import constantan.lobotomy.common.util.mixin.IMixinDamageSource;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;

public abstract class AbnormalityEntity extends Monster implements IRiskLevel, IDefense, IDamageType, IAnimatableParent {

    private static final EntityDataAccessor<Integer> QLIPHOTH_COUNTER = SynchedEntityData.defineId(AbnormalityEntity.class, EntityDataSerializers.INT);

    protected static final AnimationBuilder ANIM_IDLE = new AnimationBuilder()
            .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ANIM_FLY = new AnimationBuilder()
            .addAnimation("fly", ILoopType.EDefaultLoopTypes.LOOP);

    private final AnimationFactory factory;

    private final IMixinEntityType<?> abnormalityType;
    private final IMixinDamageSource abnormalDamageSource;

    public AbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        this.abnormalityType = ModEntityTypes.abnormalityEntityType(pEntityType);
        this.abnormalDamageSource = (IMixinDamageSource) (Object) DamageSource.mobAttack(this);

        this.resetQliphothCounter();
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.abnormalityType.getRiskLevel();
    }

    @Override
    public Map<DamageTypeUtil, Float> getAbnormalDefense() {
        return this.abnormalityType.getDefense();
    }

    @Override
    public DamageTypeUtil getDamageType() {
        return this.abnormalityType.getDamageType();
    }

    public int getMaxQliphothCounter() {
        return this.abnormalityType.getQliphothCounter();
    }

    public boolean hasQliphothCounter() {
        return this.getQliphothCounter() != 0;
    }

    public int getQliphothCounter() {
        return this.getEntityData().get(QLIPHOTH_COUNTER);
    }

    public void setQliphothCounter(int counterValue) {
        this.getEntityData().set(QLIPHOTH_COUNTER, Mth.clamp(counterValue, 0, this.getMaxQliphothCounter()));
    }

    public void resetQliphothCounter() {
        this.setQliphothCounter(this.getMaxQliphothCounter());
    }

    public void addQliphothCounter(int add) {
        this.setQliphothCounter(this.getQliphothCounter() + add);
    }

    public void subQliphothCounter(int sub) {
        this.setQliphothCounter(this.getQliphothCounter() - sub);
    }

    public DamageSource getAbnormalDamageSource(DamageTypeUtil damageType, boolean blockable) {
        return (DamageSource) (Object) this.abnormalDamageSource.riskLevel(this.getRiskLevel()).damageType(damageType).Blockable(blockable);
    }

    public boolean canDoUnblockableAttack() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        //これが読み込まれるのは親クラスのEntity
        // this.abnormalityTypeがnullなのでgetMaxQliphothCounterは使えない
        //とりあえず初期値は0
        //コンストラクタでabnormalityTypeを初期化した後クリフォトカウンターもリセットする
        this.getEntityData().define(QLIPHOTH_COUNTER, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("qliphoth_counter", this.getQliphothCounter());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setQliphothCounter(pCompound.getInt("qliphoth_counter"));
    }
}

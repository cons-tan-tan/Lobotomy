package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.util.*;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(AbnormalityEntity.class, EntityDataSerializers.INT);

    protected static final AnimationBuilder ANIM_WALK = new AnimationBuilder()
            .addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ANIM_FLY = new AnimationBuilder()
            .addAnimation("fly", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ANIM_ATTACK = new AnimationBuilder()
            .addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private static final String QLIPHOTH_COUNTER_NAME = "qliphoth_counter";

    private final AnimationFactory factory;
    private final IMixinEntityType<?> abnormalityType;

    public AbnormalityEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        this.abnormalityType = ModEntityTypes.abnormalityEntityType(pEntityType);

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
        return this.getMaxQliphothCounter() != 0;
    }

    public int getQliphothCounter() {
        return this.getEntityData().get(QLIPHOTH_COUNTER);
    }

    public void setQliphothCounter(int counterValue) {
        if (this.level.isClientSide) {
            this.getEntityData().set(QLIPHOTH_COUNTER, Mth.clamp(counterValue, 0, this.getMaxQliphothCounter()));
        }
    }

    public void resetQliphothCounter() {
        if (this.level.isClientSide) {
            this.setQliphothCounter(this.getMaxQliphothCounter());
        }
    }

    public void addQliphothCounter(int add) {
        if (this.level.isClientSide) {
            this.setQliphothCounter(this.getQliphothCounter() + add);
        }
    }

    public void subQliphothCounter(int sub) {
        if (this.level.isClientSide) {
            this.setQliphothCounter(this.getQliphothCounter() - sub);
        }
    }

    public int getAttackTick() {
        return this.getEntityData().get(ATTACK_TICK);
    }

    public void setAttackTick(int tick) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(ATTACK_TICK, tick);
        }
    }

    /**
     * 攻撃したLivingEntityにノックバックを発生させるか<br>
     * デフォルトはtrue<br>
     * 攻撃毎に参照<br>
     * 盾で防いだ場合のノックバックの有無でも参照{@link AbnormalityEntity#blockedByShield(LivingEntity)}
     */
    public boolean canDoKnockbackAttack() {
        return true;
    }

    /**
     * 攻撃を盾で防げるか<br>
     * デフォルトはfalse<br>
     * 攻撃毎に参照
     */
    public boolean canDoUnblockableAttack() {
        return false;
    }

    @Override
    protected void blockedByShield(LivingEntity pDefender) {
        if (this.canDoKnockbackAttack()) {
            super.blockedByShield(pDefender);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setAttackTick(Math.max(this.getAttackTick() - 1, 0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        //これが読み込まれるのは親クラスのEntity
        // this.abnormalityTypeがnullなのでgetMaxQliphothCounterは使えない
        //とりあえず初期値は0
        //コンストラクタでabnormalityTypeを初期化した後クリフォトカウンターもリセットする
        this.getEntityData().define(QLIPHOTH_COUNTER, 0);
        this.getEntityData().define(ATTACK_TICK, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(QLIPHOTH_COUNTER_NAME, this.getQliphothCounter());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setQliphothCounter(pCompound.getInt(QLIPHOTH_COUNTER_NAME));
    }
}

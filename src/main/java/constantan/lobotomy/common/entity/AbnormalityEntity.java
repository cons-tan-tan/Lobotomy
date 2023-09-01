package constantan.lobotomy.common.entity;

import constantan.lobotomy.common.init.ModEntityTypes;
import constantan.lobotomy.common.util.*;
import constantan.lobotomy.common.util.mixin.IMixinEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class AbnormalityEntity<T extends AbnormalityEntity<T>> extends Monster
        implements IRiskLevel, IDefense, IDamageType, IAnimatableParent {

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData
            .defineId(AbnormalityEntity.class, EntityDataSerializers.INT);

    protected static final AnimationBuilder ANIM_WALK = new AnimationBuilder()
            .addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ANIM_FLY = new AnimationBuilder()
            .addAnimation("fly", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ANIM_ATTACK = new AnimationBuilder()
            .addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private final AnimationFactory factory;
    private final IMixinEntityType<T> abnormalityType;

    public final Predicate<T> isAttackAnimating = abnormality -> abnormality.getAttackTick() > 0;

    public AbnormalityEntity(EntityType<T> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        this.abnormalityType = ModEntityTypes.abnormalityEntityType(pEntityType);
        if (this instanceof IQliphoth iQliphoth) {
            //内部でthis.abnormalityを参照しているので、this.abnormalityを初期化した後に呼ぶ
            iQliphoth.resetQliphothCounter();
        }

        if (this instanceof ILazyControl<?> iLazyControl) {
            this.lookControl = iLazyControl.createLazyLookControl();
            this.moveControl = iLazyControl.createLazyMoveControl();
        }
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

    public IMixinEntityType<T> getAbnormalityType() {
        return this.abnormalityType;
    }

    public boolean hasQliphothCounter() {
        return this instanceof IQliphoth;
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
        return this.abnormalityType.canKnockbackAttack();
    }

    /**
     * 盾で防げない貫通攻撃かどうか<br>
     * デフォルトはfalse<br>
     * 攻撃毎に参照
     */
    public boolean canDoUnblockableAttack() {
        return this.abnormalityType.canUnblockableAttack();
    }

    @Override
    protected void blockedByShield(@NotNull LivingEntity pDefender) {
        if (this.canDoKnockbackAttack()) {
            super.blockedByShield(pDefender);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setAttackTick(Math.max(this.getAttackTick() - 1, 0));

        //ServerSide
        if (this instanceof ISyncSpontaneousMoving iSyncSpontaneousMoving) {
            iSyncSpontaneousMoving.syncSpontaneousMovingTick();
        }

        //ClientSide
        if (this instanceof ITransientNoCulling<?> iTransientNoCulling) {
            iTransientNoCulling.noCullingTick();
        }
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        if (this instanceof ILazyControl<?> iLazyControl) {
            return iLazyControl.createLazyBodyControl();
        }
        return super.createBodyControl();
    }

    @Override
    public void setId(int pId) {
        super.setId(pId);
        if (this instanceof IMultiPart iMultiPart) {
            for (int i = 0; i < iMultiPart.getSubParts().length; i++) {
                iMultiPart.getSubParts()[i].setId(pId + i + 1);
            }
        }
    }

    @Override
    public boolean isMultipartEntity() {
        return this instanceof IMultiPart || super.isMultipartEntity();
    }

    @Nullable
    @Override
    public PartEntity<?>[] getParts() {
        return this instanceof IMultiPart iMultiPart ? iMultiPart.getSubParts() : super.getParts();
    }

    @Override
    public boolean isPickable() {
        return !(this instanceof IMultiPart) && super.isPickable();
    }

    @Override
    public boolean isPushable() {
        return !(this instanceof IMultiPart) && super.isPushable();
    }

    @Override
    protected void pushEntities() {
        if (this instanceof IMultiPart iMultiPart) {
            boolean flag = false;
            for (PartEntity<?> part : iMultiPart.getSubParts()) {
                List<Entity> list = part.level.getEntities(part, part.getBoundingBox(), EntitySelector.pushableBy(part));
                if (!list.isEmpty()) {
                    int i = part.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
                    if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                        int j = 0;

                        for (Entity entity : list) {
                            if (!entity.isPassenger()) {
                                ++j;
                            }
                        }

                        if (j > i - 1 && !flag) {
                            flag = part.hurt(DamageSource.CRAMMING, 6.0F);
                        }
                    }

                    for (Entity entity : list) {
                        entity.push(part);
                    }
                }
            }
        } else {
            super.pushEntities();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        //これが読み込まれるのは親クラスのEntityなので、this.abnormalityTypeがnull
        //getMaxQliphothCounterは使えないので初期値は0、コンストラクタでabnormalityTypeを初期化した後クリフォトカウンターもリセットする
        if (this.hasQliphothCounter()) {
            this.getEntityData().define(IQliphoth.QLIPHOTH_COUNTER, 0);
        }
        if (this instanceof ISyncSpontaneousMoving) {
            this.getEntityData().define(ISyncSpontaneousMoving.IS_MOVING_SPONTANEOUSLY, false);
        }
        this.getEntityData().define(ATTACK_TICK, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this instanceof IQliphoth iQliphoth) {
            pCompound.putInt(IQliphoth.QLIPHOTH_COUNTER_NAME, iQliphoth.getQliphothCounter());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this instanceof IQliphoth iQliphoth) {
            iQliphoth.setQliphothCounter(pCompound.getInt(IQliphoth.QLIPHOTH_COUNTER_NAME));
        }
    }
}

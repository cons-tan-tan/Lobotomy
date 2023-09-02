package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.ModItemRenderers;
import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.common.item.util.IEgo;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.IDamageType;
import constantan.lobotomy.common.item.util.ISyncableParent;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Random;
import java.util.function.Consumer;

public abstract class EgoWeapon extends Item implements IEgo, IDamageType, ISyncableParent {

    private final AnimationFactory factory;

    private final int minDamageAmount;
    private final int maxDamageAmount;
    private final DamageTypeUtil damageType;
    private final RiskLevelUtil riskLevel;

    private final TextComponent abnormalDamageTooltip;

    protected final boolean hasIdleAnim;

    @SuppressWarnings("unchecked")
    public <E extends ForgeRegistryEntry<E>, T extends ForgeRegistryEntry<E> & ISyncable> EgoWeapon(int minDamage, int maxDamage, Properties pProperties) {
        super(pProperties.tab(ModSetup.CREATIVE_TAB).stacksTo(1));
        this.minDamageAmount = minDamage;
        this.maxDamageAmount = maxDamage;

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        if (this instanceof ISyncable) {
            GeckoLibNetwork.registerSyncable((T) this);
        }

        var egoWeaponItemProperties = (EgoWeaponProperties<?>) pProperties;
        this.riskLevel = egoWeaponItemProperties.riskLevel;
        this.damageType = egoWeaponItemProperties.damageType;
        this.hasIdleAnim = egoWeaponItemProperties.idleAnim;

        this.abnormalDamageTooltip = this.getDamageType().getColoredTextComponentWithValue(minDamage, maxDamage, this instanceof EgoRangeWeapon);
    }

    public void playAnimation(LivingEntity entity, InteractionHand hand, int state) {
        ItemStack stack = entity.getItemInHand(hand);
        playAnimation(entity, stack, state);
    }

    public int getRangedRandomDamage(ItemStack stack) {
        return new Random().nextInt(this.minDamageAmount, this.maxDamageAmount + 1);
    }

    public int getMinDamageAmount(ItemStack stack) {
        return this.minDamageAmount;
    }

    public int getMaxDamageAmount(ItemStack stack) {
        return this.maxDamageAmount;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void playAnimation(LivingEntity entity, ItemStack stack, int state) {
        if (!entity.level.isClientSide && this instanceof ISyncable iSyncable) {
            int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) entity.level);
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
            GeckoLibNetwork.syncAnimation(target, iSyncable, id, state);
        }
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Override
    public DamageTypeUtil getDamageType() {
        return this.damageType;
    }

    public TextComponent getAbnormalDamageTooltip(ItemStack stack) {
        return this.abnormalDamageTooltip;
    }

    /**
     * クリエで左クリックしたときのブロック破壊を無くす
     */
    @Override
    public boolean canAttackBlock(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        if (this instanceof IAnimatable) {
            consumer.accept(new IItemRenderProperties() {
                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return ModItemRenderers.getRenderer(EgoWeapon.this);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static class EgoWeaponProperties<R> extends EgoProperties<R> {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
        DamageTypeUtil damageType = DamageTypeUtil.RED;
        boolean idleAnim;

        @Override
        public R riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return (R) this;
        }

        @Override
        public R idleAnim() {
            this.idleAnim = true;
            return (R) this;
        }

        public R damageType(DamageTypeUtil damageType) {
            this.damageType = damageType;
            return (R) this;
        }
    }
}

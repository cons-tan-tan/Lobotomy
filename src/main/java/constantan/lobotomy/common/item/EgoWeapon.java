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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class EgoWeapon extends Item implements IEgo, IDamageType, ISyncableParent {

    private final AnimationFactory factory;

    private final DamageTypeUtil damageType;
    private final RiskLevelUtil riskLevel;

    public <E extends ForgeRegistryEntry<E>, T extends ForgeRegistryEntry<E> & ISyncable> EgoWeapon(Properties pProperties) {
        super(pProperties.tab(ModSetup.CREATIVE_TAB));

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        if (this instanceof ISyncable) {
            GeckoLibNetwork.registerSyncable((T) this);
        }

        EgoWeaponProperties egoWeaponItemProperties = (EgoWeaponProperties) pProperties;
        this.riskLevel = egoWeaponItemProperties.riskLevel;
        this.damageType = egoWeaponItemProperties.damageType;
    }

    public void playAnimation(LivingEntity entity, InteractionHand hand, int state) {
        ItemStack stack = entity.getItemInHand(hand);
        playAnimation(entity, stack, state);
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

    /**
     * クリエで左クリックしたときのブロック破壊を無くす
     */
    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ModItemRenderers.getRenderer(EgoWeapon.this);
            }
        });
    }

    public static class EgoWeaponProperties extends EgoProperties {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
        DamageTypeUtil damageType = DamageTypeUtil.RED;

        public EgoProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public EgoWeaponProperties damageType(DamageTypeUtil damageType) {
            this.damageType = damageType;
            return this;
        }
    }
}

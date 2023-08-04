package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.ModItemRenderers;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.IDamageType;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class EgoWeaponItem extends Item implements IEgo , IDamageType {

    private final AnimationFactory factory;

    private final DamageTypeUtil damageType;
    private final RiskLevelUtil riskLevel;

    public <E extends ForgeRegistryEntry<E>, T extends ForgeRegistryEntry<E> & ISyncable> EgoWeaponItem(Properties pProperties) {
        super(pProperties);

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        if (this instanceof ISyncable) {
            GeckoLibNetwork.registerSyncable((T) this);
        }

        EgoWeaponItemProperties egoWeaponItemProperties = (EgoWeaponItemProperties) pProperties;
        this.riskLevel = egoWeaponItemProperties.riskLevel;
        this.damageType = egoWeaponItemProperties.damageType;
    }

    /**
     * ISyncable専用
     */
    public void playAnimation(LivingEntity entity, InteractionHand hand, int state) {
        ItemStack stack = entity.getItemInHand(hand);
        playAnimation(entity, stack, state);
    }

    /**
     * ISyncable専用
     */
    public void playAnimation(LivingEntity entity, ItemStack stack, int state) {
        if (!entity.level.isClientSide && this instanceof ISyncable iSyncable) {
            int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) entity.level);
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
            GeckoLibNetwork.syncAnimation(target, iSyncable, id, state);
        }
    }

    /**
     * IAnimatable専用<br><br>
     * {@link IAnimatable#registerControllers}で登録した{@link AnimationController}を{@link ISyncable#onAnimationSync}で制御する場合transitionLengthTicksを1以上にしてないとクラッシュするので注意
     */
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Override
    public DamageTypeUtil getDamageType() {
        return this.damageType;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ModItemRenderers.getRenderer(EgoWeaponItem.this);
            }
        });
    }

    public static class EgoWeaponItemProperties extends EgoItemProperties {

        DamageTypeUtil damageType = DamageTypeUtil.RED;

        public EgoWeaponItemProperties damageType(DamageTypeUtil damageType) {
            this.damageType = damageType;
            return this;
        }
    }
}

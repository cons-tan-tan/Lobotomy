package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.ModItemRenderers;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class EgoWeaponItem extends Item implements IEgo {

    public final AnimationFactory factory;

    public final DamageTypeUtil damageType;
    public final RiskLevelUtil riskLevel;

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

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    /**
     * onAnimationSyncで制御するAnimationControllerはtransitionLengthTicksを1以上にしてないとクラッシュするから注意!!!!
     */
    public abstract void registerControllers(AnimationData data);

    public AnimationFactory getFactory() {
        return this.factory;
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

package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.armor.EgoArmorRenderer;
import constantan.lobotomy.common.item.util.EgoArmorMaterial;
import constantan.lobotomy.common.item.util.IEgo;
import constantan.lobotomy.common.item.util.ISyncableParent;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.IDefense;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;

public abstract class EgoArmor extends ArmorItem implements IEgo, IDefense, ISyncableParent {

    protected static final AnimationBuilder ANIM_IDLE = new AnimationBuilder()
            .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);

    private final AnimationFactory factory;

    private final Map<DamageTypeUtil, Float> defense;
    private final RiskLevelUtil riskLevel;

    public EgoArmor(Properties builder) {
        super(new EgoArmorMaterial((EgoArmorProperties) builder), EquipmentSlot.CHEST, builder);

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        EgoArmorProperties egoArmorItemProperties = (EgoArmorProperties) builder;
        this.riskLevel = egoArmorItemProperties.riskLevel;
        this.defense = egoArmorItemProperties.defense;
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Override
    public Map<DamageTypeUtil, Float> getAbnormalDefense() {
        return this.defense;
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
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                  EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return (HumanoidModel<?>) EgoArmorRenderer.getEgoArmorRenderer(EgoArmor.this, entityLiving)
                        .applyEntityStats(_default).setCurrentItem(entityLiving, itemStack, armorSlot)
                        .applySlot(armorSlot);
            }
        });
    }

    @Override
    public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        EgoArmorRenderer renderer = EgoArmorRenderer.getEgoArmorRenderer(EgoArmor.this, entity);
        return renderer.getTextureLocation((ArmorItem) stack.getItem()).toString();
    }

    public static class EgoArmorProperties extends EgoProperties {

        public Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

        public String materialName;

        public EgoArmorProperties defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }

        public EgoArmorProperties name(String materialName) {
            this.materialName = materialName;
            return this;
        }
    }
}
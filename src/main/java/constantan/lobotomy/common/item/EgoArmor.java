package constantan.lobotomy.common.item;

import constantan.lobotomy.client.renderer.entity.layer.EgoSuitLayer;
import constantan.lobotomy.client.renderer.armor.EgoArmorRenderer;
import constantan.lobotomy.common.ModSetup;
import constantan.lobotomy.common.item.util.EgoArmorMaterial;
import constantan.lobotomy.common.item.util.IEgo;
import constantan.lobotomy.common.item.util.ISyncableParent;
import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.IDefense;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class EgoArmor extends ArmorItem implements IEgo, IDefense, ISyncableParent {

    private final AnimationFactory factory;

    private final RiskLevelUtil riskLevel;
    private final Map<DamageTypeUtil, Float> defense;
    private final boolean suitTexture;
    private final Set<EgoSuitLayer.SuitInnerPart> innerPartSet;
    protected final boolean hasIdleAnim;

    private final TextComponent defenseMultiplierTooltip;
    private final TextComponent highlightedDefenseMultiplierTooltip;

    public EgoArmor(Properties builder) {
        super(new EgoArmorMaterial(
                ((EgoArmorProperties) builder).riskLevel,
                ((EgoArmorProperties) builder).defense
        ), EquipmentSlot.CHEST, builder.tab(ModSetup.CREATIVE_TAB));

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        var egoArmorItemProperties = (EgoArmorProperties) builder;
        this.riskLevel = egoArmorItemProperties.riskLevel;
        this.defense = egoArmorItemProperties.defense;
        this.suitTexture = egoArmorItemProperties.suitTexture;
        this.innerPartSet = new HashSet<>(egoArmorItemProperties.innerPartSet);
        this.hasIdleAnim = egoArmorItemProperties.idleAnim;

        this.defenseMultiplierTooltip = DefenseUtil.getDefenseMultiplierTextComponent(egoArmorItemProperties.defense);
        this.highlightedDefenseMultiplierTooltip = DefenseUtil.getDefenseMultiplierTextComponent(egoArmorItemProperties.defense, true);
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

    public TextComponent getDefenseMultiplierTooltip() {
        if (Screen.hasShiftDown()) {
            return this.highlightedDefenseMultiplierTooltip;
        } else {
            return this.defenseMultiplierTooltip;
        }
    }

    public boolean hasSuitTexture() {
        return this.suitTexture;
    }

    public boolean hasInnerPart() {
        return !this.innerPartSet.isEmpty();
    }

    public Set<EgoSuitLayer.SuitInnerPart> getInnerPartSet() {
        return this.innerPartSet;
    }

    @Nullable
    public ResourceLocation getSuitTexture(LivingEntity livingEntity) {
        if (this instanceof IAnimatable) {
            var renderer = EgoArmorRenderer.getEgoArmorRenderer(this, livingEntity);
            return renderer.egoArmorModel.getSuitTextureLocation(this);
        }
        return null;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        if (this instanceof IAnimatable) {
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
    }

    @Override
    public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this instanceof IAnimatable) {
            var renderer = EgoArmorRenderer.getEgoArmorRenderer(EgoArmor.this, entity);
            return renderer.getTextureLocation(this).toString();
        }
        return super.getArmorTexture(stack, entity, slot, type);
    }

    public static class EgoArmorProperties extends EgoProperties<EgoArmorProperties> {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
        Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;
        boolean suitTexture = true;
        Set<EgoSuitLayer.SuitInnerPart> innerPartSet = new HashSet<>();
        boolean idleAnim;

        @Override
        public EgoArmorProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        @Override
        public EgoArmorProperties idleAnim() {
            this.idleAnim = true;
            return this;
        }

        public EgoArmorProperties defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }

        public EgoArmorProperties noSuitTexture() {
            this.suitTexture = false;
            return this;
        }

        public EgoArmorProperties suitInnerPart(EgoSuitLayer.SuitInnerPart suitInnerPart) {
            this.innerPartSet.add(suitInnerPart);
            return this;
        }
    }
}

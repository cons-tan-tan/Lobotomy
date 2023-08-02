package constantan.lobotomy.common.item;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.IDefense;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;

public abstract class EgoArmorItem extends GeoArmorItem implements IEgo, IDefense {

    public final AnimationFactory factory;

    public final Map<DamageTypeUtil, Float> defense;
    public final RiskLevelUtil riskLevel;

    public EgoArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        EgoArmorItemProperties egoArmorItemProperties = (EgoArmorItemProperties) builder;
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

    public AnimationFactory getFactory() {
        return this.factory;
    }


    public static class EgoArmorItemProperties extends EgoItemProperties {

        Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

        public EgoArmorItemProperties defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }
    }
}

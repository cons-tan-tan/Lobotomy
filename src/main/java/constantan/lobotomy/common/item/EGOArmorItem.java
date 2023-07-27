package constantan.lobotomy.common.item;

import constantan.lobotomy.common.util.DamageTypeUtil;
import constantan.lobotomy.common.util.IRiskLevel;
import constantan.lobotomy.common.util.DefenseUtil;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class EGOArmorItem extends GeoArmorItem implements IRiskLevel, IAnimatable {

    public final AnimationFactory FACTORY;

    public Map<DamageTypeUtil, Float> defense;
    public RiskLevelUtil riskLevel;

    public EGOArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);

        this.FACTORY = GeckoLibUtil.createFactory(this);

        this.riskLevel = RiskLevelUtil.ZAYIN;
        this.defense = new HashMap<>(DefenseUtil.DEFAULT_DEFENSE);
    }

    @Override
    public RiskLevelUtil getRiskLevel() {
        return this.riskLevel;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.FACTORY;
    }


    public static class EGOArmorItemProperties extends Properties {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;
        Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

        public EGOArmorItemProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public EGOArmorItemProperties defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }
    }
}

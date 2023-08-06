package constantan.lobotomy.common.item;

import constantan.lobotomy.common.item.util.EgoArmorMaterial;
import constantan.lobotomy.common.item.util.IEgo;
import constantan.lobotomy.common.item.util.ISyncableParent;
import constantan.lobotomy.common.util.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;

public abstract class EgoArmor extends GeoArmorItem implements IEgo, IDefense, ISyncableParent {

    private final AnimationFactory factory;

    private final Map<DamageTypeUtil, Float> defense;
    private final RiskLevelUtil riskLevel;

    public EgoArmor(String materialName, Properties builder) {
        super(new EgoArmorMaterial(materialName,
                ((EgoArmorItemProperties) builder).riskLevel,
                ((EgoArmorItemProperties) builder).defense), EquipmentSlot.CHEST, builder);

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

    public static class EgoArmorItemProperties extends EgoItemProperties {

        Map<DamageTypeUtil, Float> defense = DefenseUtil.DEFAULT_DEFENSE;

        public EgoArmorItemProperties defense(float red, float white, float black, float pale) {
            this.defense = DefenseUtil.createDefense(red, white, black, pale);
            return this;
        }
    }
}

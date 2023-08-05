package constantan.lobotomy.common.item;

import constantan.lobotomy.common.util.IAbnormalityTool;
import constantan.lobotomy.common.util.ISyncableParent;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class AbnormalityToolItem extends Item implements IAbnormalityTool, ISyncableParent {

    public final AnimationFactory factory;
    public final RiskLevelUtil riskLevel;

    public AbnormalityToolItem(Item.Properties pProperties) {
        super(pProperties);

        this.factory = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        AbnormalityToolItemProperties abnormalityToolItemProperties = (AbnormalityToolItemProperties) pProperties;
        this.riskLevel = abnormalityToolItemProperties.riskLevel;
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

    public static class AbnormalityToolItemProperties extends Item.Properties {

        RiskLevelUtil riskLevel = RiskLevelUtil.ZAYIN;

        public AbnormalityToolItemProperties riskLevel(RiskLevelUtil riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }
    }
}

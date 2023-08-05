package constantan.lobotomy.common.item;

import constantan.lobotomy.common.util.IAbnormalityTool;
import constantan.lobotomy.common.util.RiskLevelUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public abstract class AbnormalityToolItem extends Item implements IAbnormalityTool {

    public final AnimationFactory FACTORY;
    public final RiskLevelUtil riskLevel;

    public AbnormalityToolItem(Item.Properties pProperties) {
        super(pProperties);

        this.FACTORY = this instanceof IAnimatable iAnimatable
                ? GeckoLibUtil.createFactory(iAnimatable)
                : null;

        AbnormalityToolItemProperties abnormalityToolItemProperties = (AbnormalityToolItemProperties) pProperties;
        this.riskLevel = abnormalityToolItemProperties.riskLevel;
    }

    public AnimationFactory getFactory() {
        return this.FACTORY;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(this.riskLevel.getColoredTextComponentsForTooltip());
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
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

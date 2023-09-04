package constantan.lobotomy.integration.jade;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import constantan.lobotomy.common.item.EgoWeapon;
import constantan.lobotomy.common.util.RiskLevelUtil;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public enum RiskLevelComponentProvider implements IEntityComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        Entity entity = entityAccessor.getEntity();
        if (entity instanceof AbnormalityEntity<?> abnormality) {
            iTooltip.add(abnormality.getDisplayRiskLevel().getColoredTextComponent());
        } else if (entity instanceof LivingEntity livingEntity) {
            LocalPlayer player = Minecraft.getInstance().player;
            boolean flag = false;
            if (player != null) {
                for (InteractionHand interactionHand : InteractionHand.values()) {
                    ItemStack stack = player.getItemInHand(interactionHand);
                    if (stack.getItem() instanceof EgoWeapon) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                iTooltip.add(RiskLevelUtil.getRiskLevel(livingEntity).getColoredTextComponent());
            }
        }
    }
}

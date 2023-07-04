package constantan.lobotomy.common.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import constantan.lobotomy.common.init.ModDamageSource;
import constantan.lobotomy.common.init.ModEffects;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.EffectRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class OwingEffect extends MobEffect {

    private static UUID UUID_TARGET = null;

    public OwingEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static UUID getTargetUUID() {
        return UUID_TARGET;
    }

    public static void setTargetUUID(UUID uuid){
        if (uuid != null) {
            UUID_TARGET = uuid;
        }
    }

    public void compensate(PotionEvent event) {
        if (event.getPotionEffect().getEffect() == ModEffects.OWING.get()
                && event.getEntityLiving() instanceof Player player
                && player.getUUID() == UUID_TARGET
                && !player.getLevel().isClientSide) {
            player.hurt(ModDamageSource.GIANT_TREE_SAP, player.getHealth());//即死
            UUID_TARGET = null;
        }
    }

    @SubscribeEvent
    public void onPotionExpired(PotionEvent.PotionExpiryEvent event) {//時間切れしたとき
        this.compensate(event);
    }

    @SubscribeEvent
    public void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {//効果が除去されたとき(Milk Bucketなど)
        this.compensate(event);
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }

    @Override
    public Object getEffectRendererInternal() {
        return new EffectRenderer() {
            @Override
            public boolean shouldRender(MobEffectInstance effect) {
                return false;
            }

            @Override
            public void renderInventoryEffect(MobEffectInstance effectInstance, EffectRenderingInventoryScreen<?> gui, PoseStack poseStack, int x, int y, float z) {

            }

            @Override
            public void renderHUDEffect(MobEffectInstance effectInstance, GuiComponent gui, PoseStack poseStack, int x, int y, float z, float alpha) {

            }
        };
    }
}

package constantan.lobotomy.common.effect;

import constantan.lobotomy.common.init.ModDamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class OwingEffect extends NoRenderMobEffect {

    public OwingEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void compensate(PotionEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide && event.getPotionEffect().getEffect() == this) {
            player.hurt(ModDamageSource.GIANT_TREE_SAP, player.getHealth());//即死
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
    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}

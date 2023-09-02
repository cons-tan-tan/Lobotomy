package constantan.lobotomy.common.item.abnormality;

import constantan.lobotomy.common.init.ModEffects;
import constantan.lobotomy.common.item.AbnormalityTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class GiantTreeSapItem extends AbnormalityTool implements IAnimatable {

    public static final float PROBABILITY_MODIFIER = 0.15F;

    public GiantTreeSapItem(Item.Properties pProperties) {
        super(pProperties.food((new FoodProperties.Builder()).alwaysEat().build()));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            player.heal(player.getMaxHealth());
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20*60, 4, false, false));
            GiantTreeSapExplosionLevelManager manager = GiantTreeSapExplosionLevelManager.get(level);
            if (player.getActiveEffectsMap().get(ModEffects.OWING.get()) == null && Math.random() < manager.getExplosionLevel() * PROBABILITY_MODIFIER) {
                player.addEffect(new MobEffectInstance(ModEffects.OWING.get(), 20*20, 0, false, false));
                manager.resetExplosionLevel();
            } else {
                manager.addExplosionLevel();
            }
        }
        return itemStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }


    public static class GiantTreeSapExplosionLevelManager extends SavedData {

        private static final String KEY = "explosion_level";

        private int explosionLevel;

        public GiantTreeSapExplosionLevelManager() {
        }

        public GiantTreeSapExplosionLevelManager(CompoundTag tag) {
            this.explosionLevel = tag.getInt(KEY);
        }

        public static GiantTreeSapExplosionLevelManager get(Level level) {
            if (level.isClientSide) {
                throw new RuntimeException("Don't access this client-side!");
            }

            DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
            return storage.computeIfAbsent(GiantTreeSapExplosionLevelManager::new,
                    GiantTreeSapExplosionLevelManager::new,
                    "giant_tree_sap_explosion_level_manager");
        }

        public int getExplosionLevel() {
            return this.explosionLevel;
        }

        public void addExplosionLevel() {
            this.explosionLevel = Math.min(this.explosionLevel + 1, 4);
            setDirty();
        }

        public void resetExplosionLevel() {
            this.explosionLevel = 0;
            setDirty();
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putInt(KEY, explosionLevel);
            return tag;
        }
    }
}

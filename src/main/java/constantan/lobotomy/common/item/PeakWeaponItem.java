package constantan.lobotomy.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.function.Predicate;

public class PeakWeaponItem extends ProjectileWeaponItem {

    public PeakWeaponItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }
}

package constantan.lobotomy.common.init;

import constantan.lobotomy.common.effect.OwingEffect;
import constantan.lobotomy.lib.LibColorCode;
import constantan.lobotomy.lib.LibEffectNames;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LibMisc.MOD_ID);

    public static final RegistryObject<MobEffect> OWING = MOB_EFFECTS.register(LibEffectNames.OWING,
            () -> new OwingEffect(MobEffectCategory.NEUTRAL, LibColorCode.OWING));
}

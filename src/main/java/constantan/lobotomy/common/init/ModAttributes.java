package constantan.lobotomy.common.init;

import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, LibMisc.MOD_ID);

    public static final RegistryObject<Attribute> EGO_DEFENSE = ATTRIBUTES.register("ego_defense",
            () -> new RangedAttribute("lobotomy.ego_defense", 0, 0, 1024));
}

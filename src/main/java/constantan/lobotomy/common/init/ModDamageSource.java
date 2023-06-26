package constantan.lobotomy.common.init;

import constantan.lobotomy.lib.LibBlockNames;
import constantan.lobotomy.lib.LibItemNames;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSource extends DamageSource {

    public ModDamageSource(String damageSource) {
        super(LibMisc.MOD_ID + "." + damageSource);
    }

    public static final DamageSource GIANT_TREE_SAP = (new ModDamageSource(LibItemNames.GIANT_TREE_SAP).bypassArmor());
}

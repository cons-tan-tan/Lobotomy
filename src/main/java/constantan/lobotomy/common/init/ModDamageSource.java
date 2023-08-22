package constantan.lobotomy.common.init;

import constantan.lobotomy.lib.LibAbnormality;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSource extends DamageSource {

    public ModDamageSource(String damageSource) {
        super(LibMisc.MOD_ID + "." + damageSource);
    }

    public static final DamageSource GIANT_TREE_SAP = (new ModDamageSource(LibAbnormality.GIANT_TREE_SAP.name()).bypassArmor().bypassMagic());
}

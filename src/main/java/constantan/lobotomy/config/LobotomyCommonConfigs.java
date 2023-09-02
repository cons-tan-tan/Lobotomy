package constantan.lobotomy.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LobotomyCommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> EGO_WEAPON_LOST;

    static {
        BUILDER.push("Configs for Lobotomy Mod");

        EGO_WEAPON_LOST = BUILDER.comment("Whether you have a chance of lost your E.G.O. weapon")
                        .define("Enable Lost", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

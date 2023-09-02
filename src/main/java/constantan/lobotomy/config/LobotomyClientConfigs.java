package constantan.lobotomy.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LobotomyClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Lobotomy Mod");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

package constantan.lobotomy.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LobotomyClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TARGET_DUMMY_SUIT_LAYER;

    static {
        BUILDER.push("Configs for Lobotomy Mod");

        TARGET_DUMMY_SUIT_LAYER = BUILDER
                .comment("Whether to add E.G.O. Suit Layer to Target Dummy in MmmMmmMmmMmm")
                .define("Enable Suit Layer", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

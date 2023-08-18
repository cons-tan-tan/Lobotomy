package constantan.lobotomy.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.capability.sanity.PlayerSanity;
import constantan.lobotomy.common.capability.sanity.PlayerSanityProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class LobotomyCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = Commands.literal("lobotomy").requires(context ->
                        context.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("sanity")
                        .then(Commands.literal("set")
                                .then(Commands.argument("sanity", IntegerArgumentType.integer()).executes(context ->
                                        setSanity(context, context.getSource().getPlayerOrException()))))
                        .then(Commands.literal("setmax")
                                .then(Commands.argument("maxsanity", IntegerArgumentType.integer(PlayerSanity.MIN_SANITY + 1)).executes(context ->
                                        setMaxSanity(context, context.getSource().getPlayerOrException())))));

        dispatcher.register(literalargumentbuilder);
    }

    private static int setSanity(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        final boolean[] flag = new boolean[1];
        player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity ->
                flag[0] = sanity.setSanityWithSync(IntegerArgumentType.getInteger(context, "sanity"), player));
        return flag[0] ? 1 : 0;
    }

    private static int setMaxSanity(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        LobotomyMod.logger.info("ok!!!!!");
        final boolean[] flag = new boolean[1];
        player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
            flag[0] = sanity.setMaxSanityWithSync(IntegerArgumentType.getInteger(context, "maxsanity"), player);
        });
        return flag[0] ? 1 : 0;
    }
}

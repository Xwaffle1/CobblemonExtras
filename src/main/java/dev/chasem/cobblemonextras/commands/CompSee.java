package dev.chasem.cobblemonextras.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.chasem.cobblemonextras.config.CobblemonExtrasConfig;
import dev.chasem.cobblemonextras.screen.CompSeeHandlerFactory;
import dev.chasem.cobblemonextras.screen.PokeSeeHandlerFactory;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CompSee {

    public CompSee() {
        register();
    }

    public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("compsee")
                            .requires(Permissions.require("cobblemonextras.command.compsee", CobblemonExtrasConfig.COMMAND_COMPSEE_PERMISSION_LEVEL))
                            .executes(this::self)
            );
            dispatcher.register(
                    literal("compseeother")
                            .requires(Permissions.require("cobblemonextras.command.compseeother", CobblemonExtrasConfig.COMMAND_COMPSEEOTHER_PERMISSION_LEVEL))
                            .then(argument("player", EntityArgumentType.player())
                                    .executes(this::other))
            );
        });
    }

    private int self(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().getPlayer() != null) {
            ServerPlayerEntity player = ctx.getSource().getPlayer();
            player.openHandledScreen(new CompSeeHandlerFactory());
        }
        return 1;
    }

    private int other(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().getPlayer() != null) {
            ServerPlayerEntity player = ctx.getSource().getPlayer();
            String otherPlayerName = ctx.getInput().split(" ")[1]; // ctx.getArgument("player", String.class);
            ServerPlayerEntity otherPlayer = ctx.getSource().getServer().getPlayerManager().getPlayer(otherPlayerName);
            if (otherPlayer != null) {
                player.openHandledScreen(new CompSeeHandlerFactory(otherPlayer));
            }
        }
        return 1;
    }

}

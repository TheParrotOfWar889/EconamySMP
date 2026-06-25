package com.economysmp.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import com.economysmp.data.PlayerDataManager;
import java.util.UUID;

public class EconomyCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerBalanceCommand(dispatcher);
            registerPayCommand(dispatcher);
            registerMoneyCommand(dispatcher);
            registerAddMoneyCommand(dispatcher);
        });
    }

    private static void registerBalanceCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("balance")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    UUID playerUuid = source.getPlayer().getUuid();
                    long coins = PlayerDataManager.getPlayerCoins(playerUuid);
                    long shards = PlayerDataManager.getPlayerShards(playerUuid);
                    
                    source.sendFeedback(
                        () -> Text.of("§6§lBalance\n§7Coins: §b" + coins + "\n§7Shards: §e" + shards),
                        false
                    );
                    return 1;
                })
        );
    }

    private static void registerPayCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("pay")
                .then(
                    CommandManager.argument("player", com.mojang.brigadier.arguments.StringArgumentType.string())
                        .then(
                            CommandManager.argument("amount", com.mojang.brigadier.arguments.LongArgumentType.longArg(1))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    String targetName = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "player");
                                    long amount = com.mojang.brigadier.arguments.LongArgumentType.getLong(context, "amount");
                                    
                                    UUID senderUuid = source.getPlayer().getUuid();
                                    long senderBalance = PlayerDataManager.getPlayerCoins(senderUuid);
                                    
                                    if (senderBalance < amount) {
                                        source.sendError(Text.of("§cYou don't have enough coins!"));
                                        return 0;
                                    }
                                    
                                    var targetPlayer = source.getServer().getPlayerManager().getPlayer(targetName);
                                    if (targetPlayer == null) {
                                        source.sendError(Text.of("§cPlayer not found!"));
                                        return 0;
                                    }
                                    
                                    UUID targetUuid = targetPlayer.getUuid();
                                    PlayerDataManager.removeCoins(senderUuid, amount);
                                    PlayerDataManager.addCoins(targetUuid, amount);
                                    
                                    source.sendFeedback(
                                        () -> Text.of("§aSuccessfully sent §b" + amount + "§a coins to §b" + targetName),
                                        false
                                    );
                                    
                                    targetPlayer.sendMessage(Text.of("§aYou received §b" + amount + "§a coins from §b" + source.getPlayer().getName().getString()), false);
                                    
                                    return 1;
                                })
                        )
                )
        );
    }

    private static void registerMoneyCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("money")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    UUID playerUuid = source.getPlayer().getUuid();
                    long coins = PlayerDataManager.getPlayerCoins(playerUuid);
                    long shards = PlayerDataManager.getPlayerShards(playerUuid);
                    
                    source.sendFeedback(
                        () -> Text.of("§6§lYour Wealth\n§7Coins: §b" + coins + "\n§7Shards: §e" + shards),
                        false
                    );
                    return 1;
                })
        );
    }

    private static void registerAddMoneyCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("addmoney")
                .requires(source -> source.hasPermissionLevel(4)) // Op only
                .then(
                    CommandManager.argument("player", com.mojang.brigadier.arguments.StringArgumentType.string())
                        .then(
                            CommandManager.argument("amount", com.mojang.brigadier.arguments.LongArgumentType.longArg(1))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    String targetName = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "player");
                                    long amount = com.mojang.brigadier.arguments.LongArgumentType.getLong(context, "amount");
                                    
                                    var targetPlayer = source.getServer().getPlayerManager().getPlayer(targetName);
                                    if (targetPlayer == null) {
                                        source.sendError(Text.of("§cPlayer not found!"));
                                        return 0;
                                    }
                                    
                                    UUID targetUuid = targetPlayer.getUuid();
                                    PlayerDataManager.addCoins(targetUuid, amount);
                                    
                                    source.sendFeedback(
                                        () -> Text.of("§aAdded §b" + amount + "§a coins to §b" + targetName),
                                        false
                                    );
                                    
                                    return 1;
                                })
                        )
                )
        );
    }
}

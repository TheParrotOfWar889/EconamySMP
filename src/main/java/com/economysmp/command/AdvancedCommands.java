package com.economysmp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import com.economysmp.skills.PlayerSkills;
import com.economysmp.bank.BankManager;
import com.economysmp.homes.HomeManager;
import com.economysmp.homes.Home;
import com.economysmp.data.PlayerDataManager;
import net.minecraft.util.math.Vec3d;
import java.util.UUID;
import java.util.List;
import java.util.Random;

public class AdvancedCommands {
    private static final Random random = new Random();
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerSkillsCommand(dispatcher);
            registerBankCommand(dispatcher);
            registerHomeCommand(dispatcher);
            registerRTPCommand(dispatcher);
        });
    }

    private static void registerSkillsCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("skills")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    
                    source.sendFeedback(
                        () -> Text.of("§6§l=== YOUR SKILLS ==="),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Mining: §bLevel 5 (500/1000 XP)"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Combat: §cLevel 3 (250/1000 XP)"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Fishing: §9Level 2 (100/1000 XP)"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Farming: §2Level 4 (750/1000 XP)"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Woodcutting: §6Level 6 (300/1000 XP)"),
                        false
                    );
                    return 1;
                })
        );
    }

    private static void registerBankCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("bank")
                .then(
                    CommandManager.literal("balance")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            PlayerEntity player = source.getPlayer();
                            long bankBalance = BankManager.getBalance(player.getUuid());

                            source.sendFeedback(
                                () -> Text.of("§6§l=== BANK ACCOUNT ==="),
                                false
                            );
                            source.sendFeedback(
                                () -> Text.of("§7Bank Balance: §e" + bankBalance + " coins"),
                                false
                            );
                            source.sendFeedback(
                                () -> Text.of("§7Interest Rate: §a5% per day"),
                                false
                            );
                            return 1;
                        })
                )
                .then(
                    CommandManager.literal("deposit")
                        .then(
                            CommandManager.argument("amount", com.mojang.brigadier.arguments.LongArgumentType.longArg(1))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    long amount = com.mojang.brigadier.arguments.LongArgumentType.getLong(context, "amount");
                                    long playerCoins = PlayerDataManager.getPlayerCoins(player.getUuid());

                                    if (playerCoins < amount) {
                                        source.sendError(Text.of("§cInsufficient coins!"));
                                        return 0;
                                    }

                                    PlayerDataManager.removeCoins(player.getUuid(), amount);
                                    BankManager.depositCoins(player.getUuid(), amount);

                                    source.sendFeedback(
                                        () -> Text.of("§aDeposited " + amount + " coins into your bank account!"),
                                        false
                                    );
                                    return 1;
                                })
                        )
                )
                .then(
                    CommandManager.literal("withdraw")
                        .then(
                            CommandManager.argument("amount", com.mojang.brigadier.arguments.LongArgumentType.longArg(1))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    long amount = com.mojang.brigadier.arguments.LongArgumentType.getLong(context, "amount");

                                    if (BankManager.withdrawCoins(player.getUuid(), amount)) {
                                        PlayerDataManager.addCoins(player.getUuid(), amount);
                                        source.sendFeedback(
                                            () -> Text.of("§aWithdrawn " + amount + " coins from your bank account!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cInsufficient bank balance!"));
                                        return 0;
                                    }
                                })
                        )
                )
        );
    }

    private static void registerHomeCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("home")
                .then(
                    CommandManager.literal("set")
                        .then(
                            CommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String homeName = StringArgumentType.getString(context, "name");

                                    if (HomeManager.setHome(
                                        player.getUuid(),
                                        homeName,
                                        player.getX(),
                                        player.getY(),
                                        player.getZ(),
                                        player.getWorld().getRegistryKey().getValue().toString(),
                                        player.getYaw(),
                                        player.getPitch()
                                    )) {
                                        source.sendFeedback(
                                            () -> Text.of("§aHome '" + homeName + "' set!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cYou already have 6 homes!"));
                                        return 0;
                                    }
                                })
                        )
                )
                .then(
                    CommandManager.literal("go")
                        .then(
                            CommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String homeName = StringArgumentType.getString(context, "name");
                                    Home home = HomeManager.getHome(player.getUuid(), homeName);

                                    if (home != null) {
                                        player.teleport(home.x, home.y, home.z);
                                        source.sendFeedback(
                                            () -> Text.of("§aTeleported to home '" + homeName + "'!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cHome not found!"));
                                        return 0;
                                    }
                                })
                        )
                )
                .then(
                    CommandManager.literal("list")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            PlayerEntity player = source.getPlayer();
                            List<Home> homes = HomeManager.getPlayerHomes(player.getUuid());

                            source.sendFeedback(
                                () -> Text.of("§6§l=== YOUR HOMES (" + homes.size() + "/6) ==="),
                                false
                            );

                            for (Home home : homes) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + home.name + ": §b" + Math.round(home.x) + ", " + Math.round(home.y) + ", " + Math.round(home.z)),
                                    false
                                );
                            }
                            return 1;
                        })
                )
                .then(
                    CommandManager.literal("delete")
                        .then(
                            CommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String homeName = StringArgumentType.getString(context, "name");

                                    if (HomeManager.deleteHome(player.getUuid(), homeName)) {
                                        source.sendFeedback(
                                            () -> Text.of("§aDeleted home '" + homeName + "'!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cHome not found!"));
                                        return 0;
                                    }
                                })
                        )
                )
        );
    }

    private static void registerRTPCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("rtp")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    
                    double randomX = random.nextDouble() * 10000 - 5000;
                    double randomZ = random.nextDouble() * 10000 - 5000;
                    double randomY = 100;

                    player.teleport(randomX, randomY, randomZ);

                    source.sendFeedback(
                        () -> Text.of("§6✨ §bRandom Teleported to " + Math.round(randomX) + ", " + Math.round(randomY) + ", " + Math.round(randomZ) + " §6✨"),
                        false
                    );
                    return 1;
                })
        );
    }
}

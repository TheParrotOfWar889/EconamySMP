package com.economysmp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import com.economysmp.data.PlayerDataManager;
import com.economysmp.crates.CrateManager;
import com.economysmp.crates.CrateReward;
import java.util.UUID;

public class CrateCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCrateCommand(dispatcher);
            registerShardShopCommand(dispatcher);
        });
    }

    private static void registerCrateCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("crate")
                .then(
                    CommandManager.literal("open")
                        .then(
                            CommandManager.argument("type", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String crateType = StringArgumentType.getString(context, "type");
                                    UUID playerUuid = player.getUuid();
                                    String playerId = playerUuid.toString();

                                    if (!CrateManager.canOpenCrate(playerId)) {
                                        source.sendError(Text.of("§cCrate opening is on cooldown!"));
                                        return 0;
                                    }

                                    long shardCost = CrateManager.getShardCost(crateType);
                                    long playerShards = PlayerDataManager.getPlayerShards(playerUuid);

                                    if (playerShards < shardCost) {
                                        source.sendError(Text.of("§cInsufficient shards! Need: " + shardCost + ", Have: " + playerShards));
                                        return 0;
                                    }

                                    // Remove shards
                                    PlayerDataManager.removeShards(playerUuid, shardCost);
                                    
                                    // Get random reward
                                    CrateReward reward = CrateManager.getRandomReward(crateType);
                                    
                                    // Give reward coins
                                    if ("coins".equalsIgnoreCase(reward.itemId)) {
                                        PlayerDataManager.addCoins(playerUuid, reward.quantity);
                                    } else if ("shards".equalsIgnoreCase(reward.itemId)) {
                                        PlayerDataManager.addShards(playerUuid, reward.quantity);
                                    }

                                    // Animation
                                    CrateManager.setCrateOpenCooldown(playerId);
                                    
                                    source.sendFeedback(
                                        () -> Text.of("§f§m━━━━━━━━━━§r\n" +
                                            "§6✨ §bCrate Opened! §6✨\n" +
                                            CrateManager.getRewardDisplay(reward) + "\n" +
                                            "§f§m━━━━━━━━━━§r"),
                                        false
                                    );
                                    return 1;
                                })
                        )
                )
                .then(
                    CommandManager.literal("info")
                        .then(
                            CommandManager.argument("type", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    String crateType = StringArgumentType.getString(context, "type");
                                    long shardCost = CrateManager.getShardCost(crateType);
                                    String color = CrateManager.getCrateColor(crateType);

                                    source.sendFeedback(
                                        () -> Text.of(color + "§l" + crateType.toUpperCase() + " CRATE§r\n" +
                                            "§7Cost: §d" + shardCost + " Shards\n" +
                                            "§7Possible rewards depend on rarity!"),
                                        false
                                    );
                                    return 1;
                                })
                        )
                )
        );
    }

    private static void registerShardShopCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("shardshop")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    long playerShards = PlayerDataManager.getPlayerShards(player.getUuid());

                    source.sendFeedback(
                        () -> Text.of("§6§l=== SHARD SHOP ==="),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Your Shards: §d" + playerShards),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7\n§fCommon Crate: §d10 Shards"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Rare Crate: §b25 Shards"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Epic Crate: §d50 Shards"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Legendary Crate: §650 Shards"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("§7Mythic Crate: §c250 Shards"),
                        false
                    );
                    source.sendFeedback(
                        () -> Text.of("\n§7Use: §b/crate open <type>"),
                        false
                    );
                    return 1;
                })
        );
    }
}

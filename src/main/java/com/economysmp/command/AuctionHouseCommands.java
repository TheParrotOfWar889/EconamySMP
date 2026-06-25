package com.economysmp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import com.economysmp.auctionhouse.AuctionHouseManager;
import com.economysmp.auctionhouse.AuctionItem;
import com.economysmp.data.PlayerDataManager;
import java.util.UUID;
import java.util.List;

public class AuctionHouseCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerAhCommand(dispatcher);
        });
    }

    private static void registerAhCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("ah")
                .then(
                    CommandManager.literal("sell")
                        .then(
                            CommandManager.argument("item", StringArgumentType.string())
                                .then(
                                    CommandManager.argument("quantity", IntegerArgumentType.integer(1, 2147483647))
                                        .then(
                                            CommandManager.argument("price", LongArgumentType.longArg(1))
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    PlayerEntity player = source.getPlayer();
                                                    String itemId = StringArgumentType.getString(context, "item");
                                                    int quantity = IntegerArgumentType.getInteger(context, "quantity");
                                                    long startingPrice = LongArgumentType.getLong(context, "price");

                                                    AuctionItem auction = AuctionHouseManager.createAuction(
                                                        player.getUuid(),
                                                        player.getName().getString(),
                                                        itemId,
                                                        quantity,
                                                        startingPrice
                                                    );

                                                    source.sendFeedback(
                                                        () -> Text.of("§aListed auction! §7(ID: " + auction.auctionId + ")"),
                                                        false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(
                    CommandManager.literal("view")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            List<AuctionItem> auctions = AuctionHouseManager.getAllActiveAuctions();
                            
                            source.sendFeedback(
                                () -> Text.of("§6§l--- Auction House (" + auctions.size() + " active) ---"),
                                false
                            );
                            
                            for (AuctionItem auction : auctions) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + auction.auctionId + " | §e" + auction.itemId + " §7x" + auction.quantity + " | §bBid: " + auction.currentHighestBid + "c §7from §b" + auction.highestBidderName + " §7(" + auction.getTimeRemainingMinutes() + "m)"),
                                    false
                                );
                            }
                            return 1;
                        })
                )
                .then(
                    CommandManager.literal("bid")
                        .then(
                            CommandManager.argument("auctionId", StringArgumentType.string())
                                .then(
                                    CommandManager.argument("amount", LongArgumentType.longArg(1))
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            PlayerEntity player = source.getPlayer();
                                            String auctionId = StringArgumentType.getString(context, "auctionId");
                                            long bidAmount = LongArgumentType.getLong(context, "amount");

                                            AuctionItem auction = AuctionHouseManager.getAuction(auctionId);
                                            if (auction == null) {
                                                source.sendError(Text.of("§cAuction not found!"));
                                                return 0;
                                            }

                                            if (auction.status != AuctionItem.AuctionStatus.ACTIVE) {
                                                source.sendError(Text.of("§cThis auction is no longer active!"));
                                                return 0;
                                            }

                                            if (bidAmount <= auction.currentHighestBid) {
                                                source.sendError(Text.of("§cYour bid must be higher than " + auction.currentHighestBid + " coins!"));
                                                return 0;
                                            }

                                            long playerBalance = PlayerDataManager.getPlayerCoins(player.getUuid());
                                            if (playerBalance < bidAmount) {
                                                source.sendError(Text.of("§cYou don't have enough coins! Need: " + bidAmount + ", Have: " + playerBalance));
                                                return 0;
                                            }

                                            if (AuctionHouseManager.placeBid(auctionId, player.getUuid(), player.getName().getString(), bidAmount)) {
                                                source.sendFeedback(
                                                    () -> Text.of("§aSuccessfully placed bid of " + bidAmount + " coins!"),
                                                    false
                                                );
                                                return 1;
                                            } else {
                                                source.sendError(Text.of("§cFailed to place bid!"));
                                                return 0;
                                            }
                                        })
                                )
                        )
                )
                .then(
                    CommandManager.literal("cancel")
                        .then(
                            CommandManager.argument("auctionId", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String auctionId = StringArgumentType.getString(context, "auctionId");

                                    if (AuctionHouseManager.cancelAuction(auctionId, player.getUuid())) {
                                        source.sendFeedback(
                                            () -> Text.of("§aAuction cancelled!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cCannot cancel this auction. Make sure you own it and it's still active!"));
                                        return 0;
                                    }
                                })
                        )
                )
                .then(
                    CommandManager.literal("myauctions")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            PlayerEntity player = source.getPlayer();
                            List<AuctionItem> playerAuctions = AuctionHouseManager.getSellerAuctions(player.getUuid());

                            source.sendFeedback(
                                () -> Text.of("§6§l--- Your Auctions (" + playerAuctions.size() + ") ---"),
                                false
                            );

                            for (AuctionItem auction : playerAuctions) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + auction.auctionId + " | §e" + auction.itemId + " §7| Status: " + auction.status + " | Current Bid: §b" + auction.currentHighestBid + "c"),
                                    false
                                );
                            }
                            return 1;
                        })
                )
                .then(
                    CommandManager.literal("mybids")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            PlayerEntity player = source.getPlayer();
                            List<AuctionItem> playerBids = AuctionHouseManager.getPlayerBids(player.getUuid());

                            source.sendFeedback(
                                () -> Text.of("§6§l--- Your Bids (" + playerBids.size() + ") ---"),
                                false
                            );

                            for (AuctionItem auction : playerBids) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + auction.auctionId + " | §e" + auction.itemId + " §7| Your Bid: §b" + auction.currentHighestBid + "c §7(" + auction.getTimeRemainingMinutes() + "m)"),
                                    false
                                );
                            }
                            return 1;
                        })
                )
        );
    }
}

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
import com.economysmp.marketplace.MarketplaceManager;
import com.economysmp.marketplace.MarketplaceOrder;
import com.economysmp.data.PlayerDataManager;
import java.util.UUID;
import java.util.List;

public class MarketplaceCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerOrderCommand(dispatcher);
            registerBrowseCommand(dispatcher);
            registerBuyCommand(dispatcher);
            registerCancelOrderCommand(dispatcher);
            registerMyOrdersCommand(dispatcher);
        });
    }

    private static void registerOrderCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("order")
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
                                                    long pricePerItem = LongArgumentType.getLong(context, "price");

                                                    MarketplaceOrder order = MarketplaceManager.createOrder(
                                                        player.getUuid(),
                                                        player.getName().getString(),
                                                        itemId,
                                                        quantity,
                                                        pricePerItem,
                                                        MarketplaceOrder.OrderType.SELL
                                                    );

                                                    source.sendFeedback(
                                                        () -> Text.of("§aCreated sell order! §7(ID: " + order.orderId + ")"),
                                                        false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(
                    CommandManager.literal("buy")
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
                                                    long pricePerItem = LongArgumentType.getLong(context, "price");

                                                    MarketplaceOrder order = MarketplaceManager.createOrder(
                                                        player.getUuid(),
                                                        player.getName().getString(),
                                                        itemId,
                                                        quantity,
                                                        pricePerItem,
                                                        MarketplaceOrder.OrderType.BUY
                                                    );

                                                    source.sendFeedback(
                                                        () -> Text.of("§aCreated buy order! §7(ID: " + order.orderId + ")"),
                                                        false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    private static void registerBrowseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("browse")
                .then(
                    CommandManager.literal("sell")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            List<MarketplaceOrder> orders = MarketplaceManager.getOrdersByType(MarketplaceOrder.OrderType.SELL);
                            
                            source.sendFeedback(
                                () -> Text.of("§6§l--- Sell Orders (" + orders.size() + ") ---"),
                                false
                            );
                            
                            for (MarketplaceOrder order : orders) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + order.orderId + " | §e" + order.itemId + " §7x" + order.getRemainingQuantity() + " | §b" + order.pricePerItem + "c §7from §b" + order.creatorName),
                                    false
                                );
                            }
                            return 1;
                        })
                )
                .then(
                    CommandManager.literal("buy")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            List<MarketplaceOrder> orders = MarketplaceManager.getOrdersByType(MarketplaceOrder.OrderType.BUY);
                            
                            source.sendFeedback(
                                () -> Text.of("§6§l--- Buy Orders (" + orders.size() + ") ---"),
                                false
                            );
                            
                            for (MarketplaceOrder order : orders) {
                                source.sendFeedback(
                                    () -> Text.of("§7" + order.orderId + " | §e" + order.itemId + " §7x" + order.getRemainingQuantity() + " | §b" + order.pricePerItem + "c §7from §b" + order.creatorName),
                                    false
                                );
                            }
                            return 1;
                        })
                )
        );
    }

    private static void registerBuyCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("buy")
                .then(
                    CommandManager.argument("orderId", StringArgumentType.string())
                        .then(
                            CommandManager.argument("quantity", IntegerArgumentType.integer(1, 2147483647))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity buyer = source.getPlayer();
                                    String orderId = StringArgumentType.getString(context, "orderId");
                                    int quantity = IntegerArgumentType.getInteger(context, "quantity");

                                    MarketplaceOrder order = MarketplaceManager.getOrder(orderId);
                                    if (order == null) {
                                        source.sendError(Text.of("§cOrder not found!"));
                                        return 0;
                                    }

                                    if (order.status != MarketplaceOrder.OrderStatus.ACTIVE) {
                                        source.sendError(Text.of("§cThis order is no longer active!"));
                                        return 0;
                                    }

                                    long totalCost = quantity * order.pricePerItem;
                                    long buyerBalance = PlayerDataManager.getPlayerCoins(buyer.getUuid());

                                    if (buyerBalance < totalCost) {
                                        source.sendError(Text.of("§cYou don't have enough coins! Need: " + totalCost + ", Have: " + buyerBalance));
                                        return 0;
                                    }

                                    if (MarketplaceManager.completeOrderTransaction(orderId, buyer.getUuid(), buyer.getName().getString(), quantity)) {
                                        source.sendFeedback(
                                            () -> Text.of("§aSuccessfully purchased " + quantity + "x " + order.itemId + " for " + totalCost + " coins!"),
                                            false
                                        );
                                        return 1;
                                    } else {
                                        source.sendError(Text.of("§cFailed to complete transaction!"));
                                        return 0;
                                    }
                                })
                        )
                )
        );
    }

    private static void registerCancelOrderCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("cancelorder")
                .then(
                    CommandManager.argument("orderId", StringArgumentType.string())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            PlayerEntity player = source.getPlayer();
                            String orderId = StringArgumentType.getString(context, "orderId");

                            if (MarketplaceManager.cancelOrder(orderId, player.getUuid())) {
                                source.sendFeedback(
                                    () -> Text.of("§aOrder cancelled!"),
                                    false
                                );
                                return 1;
                            } else {
                                source.sendError(Text.of("§cCannot cancel this order. Make sure you own it and it's still active!"));
                                return 0;
                            }
                        })
                )
        );
    }

    private static void registerMyOrdersCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("myorders")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    List<MarketplaceOrder> playerOrders = MarketplaceManager.getPlayerOrders(player.getUuid());

                    source.sendFeedback(
                        () -> Text.of("§6§l--- Your Orders (" + playerOrders.size() + ") ---"),
                        false
                    );

                    for (MarketplaceOrder order : playerOrders) {
                        source.sendFeedback(
                            () -> Text.of("§7" + order.orderId + " | " + (order.orderType == MarketplaceOrder.OrderType.BUY ? "§bBUY" : "§aSELL") + " §7x" + order.getRemainingQuantity() + " | Status: " + order.status),
                            false
                        );
                    }
                    return 1;
                })
        );
    }
}

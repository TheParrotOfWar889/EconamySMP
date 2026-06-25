package com.economysmp.marketplace;

import net.minecraft.server.MinecraftServer;
import com.economysmp.data.PlayerDataManager;
import com.economysmp.EconomySMPMod;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class MarketplaceManager {
    private static final Map<String, MarketplaceOrder> orders = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File dataFolder;
    private static int orderCounter = 0;

    public static void init(MinecraftServer server) {
        File worldFolder = server.getWorldDirectory().toFile();
        dataFolder = new File(worldFolder, "economysmp_marketplace");
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            EconomySMPMod.LOGGER.info("Created marketplace data folder");
        }
        
        loadAllOrders();
    }

    private static void loadAllOrders() {
        if (dataFolder.listFiles() == null) return;
        
        for (File file : dataFolder.listFiles()) {
            if (file.getName().endsWith(".json")) {
                try {
                    FileReader reader = new FileReader(file);
                    MarketplaceOrder order = gson.fromJson(reader, MarketplaceOrder.class);
                    reader.close();
                    
                    if (order != null) {
                        orders.put(order.orderId, order);
                    }
                } catch (IOException e) {
                    EconomySMPMod.LOGGER.warn("Failed to load order data from " + file.getName(), e);
                }
            }
        }
        
        EconomySMPMod.LOGGER.info("Loaded " + orders.size() + " marketplace orders");
    }

    public static MarketplaceOrder createOrder(UUID creatorUuid, String creatorName, String itemId, int quantity, long pricePerItem, MarketplaceOrder.OrderType orderType) {
        String orderId = "order_" + System.currentTimeMillis() + "_" + (orderCounter++);
        MarketplaceOrder order = new MarketplaceOrder(orderId, creatorUuid, creatorName, itemId, quantity, pricePerItem, orderType);
        orders.put(orderId, order);
        saveOrder(orderId);
        return order;
    }

    public static boolean completeOrderTransaction(String orderId, UUID buyerUuid, String buyerName, int quantity) {
        MarketplaceOrder order = orders.get(orderId);
        if (order == null || order.status != MarketplaceOrder.OrderStatus.ACTIVE) {
            return false;
        }

        if (quantity > order.getRemainingQuantity()) {
            return false;
        }

        OrderTransaction transaction = new OrderTransaction(buyerUuid, buyerName, quantity, order.pricePerItem);
        order.transactions.add(transaction);

        long totalCost = transaction.totalPrice;

        PlayerDataManager.removeCoins(buyerUuid, totalCost);
        PlayerDataManager.addCoins(order.creatorUuid, totalCost);

        if (order.getRemainingQuantity() == 0) {
            order.status = MarketplaceOrder.OrderStatus.COMPLETED;
        }

        saveOrder(orderId);
        return true;
    }

    public static boolean cancelOrder(String orderId, UUID requesterUuid) {
        MarketplaceOrder order = orders.get(orderId);
        if (order == null || !order.creatorUuid.equals(requesterUuid)) {
            return false;
        }

        if (order.status != MarketplaceOrder.OrderStatus.ACTIVE) {
            return false;
        }

        order.status = MarketplaceOrder.OrderStatus.CANCELLED;
        saveOrder(orderId);
        return true;
    }

    public static void expireOldOrders() {
        for (MarketplaceOrder order : orders.values()) {
            if (order.status == MarketplaceOrder.OrderStatus.ACTIVE && order.isExpired()) {
                order.status = MarketplaceOrder.OrderStatus.EXPIRED;
                saveOrder(order.orderId);
            }
        }
    }

    public static MarketplaceOrder getOrder(String orderId) {
        return orders.get(orderId);
    }

    public static List<MarketplaceOrder> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public static List<MarketplaceOrder> getOrdersByType(MarketplaceOrder.OrderType orderType) {
        List<MarketplaceOrder> result = new ArrayList<>();
        for (MarketplaceOrder order : orders.values()) {
            if (order.orderType == orderType && order.status == MarketplaceOrder.OrderStatus.ACTIVE) {
                result.add(order);
            }
        }
        return result;
    }

    public static List<MarketplaceOrder> getOrdersByItem(String itemId) {
        List<MarketplaceOrder> result = new ArrayList<>();
        for (MarketplaceOrder order : orders.values()) {
            if (order.itemId.equals(itemId) && order.status == MarketplaceOrder.OrderStatus.ACTIVE) {
                result.add(order);
            }
        }
        return result;
    }

    public static List<MarketplaceOrder> getPlayerOrders(UUID playerUuid) {
        List<MarketplaceOrder> result = new ArrayList<>();
        for (MarketplaceOrder order : orders.values()) {
            if (order.creatorUuid.equals(playerUuid)) {
                result.add(order);
            }
        }
        return result;
    }

    private static void saveOrder(String orderId) {
        MarketplaceOrder order = orders.get(orderId);
        if (order == null) return;
        
        try {
            File orderFile = new File(dataFolder, orderId + ".json");
            FileWriter writer = new FileWriter(orderFile);
            gson.toJson(order, writer);
            writer.close();
        } catch (IOException e) {
            EconomySMPMod.LOGGER.error("Failed to save order " + orderId, e);
        }
    }
}

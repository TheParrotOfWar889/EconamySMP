package com.economysmp.marketplace;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceOrder {
    public String orderId;
    public UUID creatorUuid;
    public String creatorName;
    public String itemId;
    public int quantity;
    public long pricePerItem;
    public OrderType orderType;
    public OrderStatus status;
    public long createdTime;
    public long expirationTime;
    public List<OrderTransaction> transactions;

    public MarketplaceOrder(String orderId, UUID creatorUuid, String creatorName, String itemId, int quantity, long pricePerItem, OrderType orderType) {
        this.orderId = orderId;
        this.creatorUuid = creatorUuid;
        this.creatorName = creatorName;
        this.itemId = itemId;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.orderType = orderType;
        this.status = OrderStatus.ACTIVE;
        this.createdTime = System.currentTimeMillis();
        this.expirationTime = this.createdTime + (48 * 60 * 60 * 1000);
        this.transactions = new ArrayList<>();
    }

    public long getTotalPrice() {
        return quantity * pricePerItem;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }

    public int getRemainingQuantity() {
        int sold = 0;
        for (OrderTransaction transaction : transactions) {
            sold += transaction.quantity;
        }
        return quantity - sold;
    }

    public enum OrderType {
        BUY,
        SELL
    }

    public enum OrderStatus {
        ACTIVE,
        COMPLETED,
        CANCELLED,
        EXPIRED
    }
}

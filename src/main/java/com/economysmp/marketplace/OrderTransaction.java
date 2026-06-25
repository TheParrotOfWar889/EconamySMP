package com.economysmp.marketplace;

import java.util.UUID;

public class OrderTransaction {
    public UUID buyerUuid;
    public String buyerName;
    public int quantity;
    public long pricePerItem;
    public long totalPrice;
    public long transactionTime;

    public OrderTransaction(UUID buyerUuid, String buyerName, int quantity, long pricePerItem) {
        this.buyerUuid = buyerUuid;
        this.buyerName = buyerName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = quantity * pricePerItem;
        this.transactionTime = System.currentTimeMillis();
    }
}

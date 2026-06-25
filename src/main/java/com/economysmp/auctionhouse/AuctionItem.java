package com.economysmp.auctionhouse;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class AuctionItem {
    public String auctionId;
    public UUID sellerUuid;
    public String sellerName;
    public String itemId;
    public int quantity;
    public long startingPrice;
    public long currentHighestBid;
    public UUID highestBidderUuid;
    public String highestBidderName;
    public AuctionStatus status;
    public long createdTime;
    public long expirationTime;
    public List<AuctionBid> bids;

    public AuctionItem(String auctionId, UUID sellerUuid, String sellerName, String itemId, int quantity, long startingPrice) {
        this.auctionId = auctionId;
        this.sellerUuid = sellerUuid;
        this.sellerName = sellerName;
        this.itemId = itemId;
        this.quantity = quantity;
        this.startingPrice = startingPrice;
        this.currentHighestBid = startingPrice;
        this.highestBidderUuid = sellerUuid;
        this.highestBidderName = sellerName;
        this.status = AuctionStatus.ACTIVE;
        this.createdTime = System.currentTimeMillis();
        this.expirationTime = this.createdTime + (7 * 24 * 60 * 60 * 1000);
        this.bids = new ArrayList<>();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }

    public long getTimeRemainingMinutes() {
        return (expirationTime - System.currentTimeMillis()) / 60000;
    }

    public enum AuctionStatus {
        ACTIVE,
        SOLD,
        EXPIRED,
        CANCELLED
    }
}

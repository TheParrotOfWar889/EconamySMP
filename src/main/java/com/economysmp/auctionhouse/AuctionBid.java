package com.economysmp.auctionhouse;

import java.util.UUID;

public class AuctionBid {
    public UUID bidderUuid;
    public String bidderName;
    public long bidAmount;
    public long bidTime;

    public AuctionBid(UUID bidderUuid, String bidderName, long bidAmount) {
        this.bidderUuid = bidderUuid;
        this.bidderName = bidderName;
        this.bidAmount = bidAmount;
        this.bidTime = System.currentTimeMillis();
    }
}

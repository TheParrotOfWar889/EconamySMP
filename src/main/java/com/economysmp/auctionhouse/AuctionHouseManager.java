package com.economysmp.auctionhouse;

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

public class AuctionHouseManager {
    private static final Map<String, AuctionItem> auctions = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File dataFolder;
    private static int auctionCounter = 0;

    public static void init(MinecraftServer server) {
        File worldFolder = server.getWorldDirectory().toFile();
        dataFolder = new File(worldFolder, "economysmp_auctionhouse");
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            EconomySMPMod.LOGGER.info("Created auction house data folder");
        }
        
        loadAllAuctions();
    }

    private static void loadAllAuctions() {
        if (dataFolder.listFiles() == null) return;
        
        for (File file : dataFolder.listFiles()) {
            if (file.getName().endsWith(".json")) {
                try {
                    FileReader reader = new FileReader(file);
                    AuctionItem auction = gson.fromJson(reader, AuctionItem.class);
                    reader.close();
                    
                    if (auction != null) {
                        auctions.put(auction.auctionId, auction);
                    }
                } catch (IOException e) {
                    EconomySMPMod.LOGGER.warn("Failed to load auction data from " + file.getName(), e);
                }
            }
        }
        
        EconomySMPMod.LOGGER.info("Loaded " + auctions.size() + " auction house listings");
    }

    public static AuctionItem createAuction(UUID sellerUuid, String sellerName, String itemId, int quantity, long startingPrice) {
        String auctionId = "auction_" + System.currentTimeMillis() + "_" + (auctionCounter++);
        AuctionItem auction = new AuctionItem(auctionId, sellerUuid, sellerName, itemId, quantity, startingPrice);
        auctions.put(auctionId, auction);
        saveAuction(auctionId);
        return auction;
    }

    public static boolean placeBid(String auctionId, UUID bidderUuid, String bidderName, long bidAmount) {
        AuctionItem auction = auctions.get(auctionId);
        if (auction == null || auction.status != AuctionItem.AuctionStatus.ACTIVE) {
            return false;
        }

        if (bidAmount <= auction.currentHighestBid) {
            return false;
        }

        long playerBalance = PlayerDataManager.getPlayerCoins(bidderUuid);
        if (playerBalance < bidAmount) {
            return false;
        }

        // Refund previous highest bidder if not the seller
        if (auction.highestBidderUuid != null && !auction.highestBidderUuid.equals(auction.sellerUuid)) {
            PlayerDataManager.addCoins(auction.highestBidderUuid, auction.currentHighestBid);
        }

        // Charge new bidder
        PlayerDataManager.removeCoins(bidderUuid, bidAmount);

        // Record bid
        AuctionBid bid = new AuctionBid(bidderUuid, bidderName, bidAmount);
        auction.bids.add(bid);
        auction.currentHighestBid = bidAmount;
        auction.highestBidderUuid = bidderUuid;
        auction.highestBidderName = bidderName;

        saveAuction(auctionId);
        return true;
    }

    public static boolean cancelAuction(String auctionId, UUID requesterUuid) {
        AuctionItem auction = auctions.get(auctionId);
        if (auction == null || !auction.sellerUuid.equals(requesterUuid)) {
            return false;
        }

        if (auction.status != AuctionItem.AuctionStatus.ACTIVE) {
            return false;
        }

        // Refund highest bidder if not seller
        if (!auction.highestBidderUuid.equals(auction.sellerUuid)) {
            PlayerDataManager.addCoins(auction.highestBidderUuid, auction.currentHighestBid);
        }

        auction.status = AuctionItem.AuctionStatus.CANCELLED;
        saveAuction(auctionId);
        return true;
    }

    public static void completeAuction(String auctionId) {
        AuctionItem auction = auctions.get(auctionId);
        if (auction == null) return;

        if (!auction.highestBidderUuid.equals(auction.sellerUuid)) {
            // Transfer coins to seller
            PlayerDataManager.addCoins(auction.sellerUuid, auction.currentHighestBid);
        }

        auction.status = AuctionItem.AuctionStatus.SOLD;
        saveAuction(auctionId);
    }

    public static void expireOldAuctions() {
        for (AuctionItem auction : auctions.values()) {
            if (auction.status == AuctionItem.AuctionStatus.ACTIVE && auction.isExpired()) {
                completeAuction(auction.auctionId);
            }
        }
    }

    public static AuctionItem getAuction(String auctionId) {
        return auctions.get(auctionId);
    }

    public static List<AuctionItem> getAllActiveAuctions() {
        List<AuctionItem> result = new ArrayList<>();
        for (AuctionItem auction : auctions.values()) {
            if (auction.status == AuctionItem.AuctionStatus.ACTIVE) {
                result.add(auction);
            }
        }
        return result;
    }

    public static List<AuctionItem> getAuctionsByItem(String itemId) {
        List<AuctionItem> result = new ArrayList<>();
        for (AuctionItem auction : auctions.values()) {
            if (auction.itemId.equals(itemId) && auction.status == AuctionItem.AuctionStatus.ACTIVE) {
                result.add(auction);
            }
        }
        return result;
    }

    public static List<AuctionItem> getSellerAuctions(UUID sellerUuid) {
        List<AuctionItem> result = new ArrayList<>();
        for (AuctionItem auction : auctions.values()) {
            if (auction.sellerUuid.equals(sellerUuid)) {
                result.add(auction);
            }
        }
        return result;
    }

    public static List<AuctionItem> getPlayerBids(UUID bidderUuid) {
        List<AuctionItem> result = new ArrayList<>();
        for (AuctionItem auction : auctions.values()) {
            if (auction.highestBidderUuid.equals(bidderUuid)) {
                result.add(auction);
            }
        }
        return result;
    }

    private static void saveAuction(String auctionId) {
        AuctionItem auction = auctions.get(auctionId);
        if (auction == null) return;
        
        try {
            File auctionFile = new File(dataFolder, auctionId + ".json");
            FileWriter writer = new FileWriter(auctionFile);
            gson.toJson(auction, writer);
            writer.close();
        } catch (IOException e) {
            EconomySMPMod.LOGGER.error("Failed to save auction " + auctionId, e);
        }
    }
}

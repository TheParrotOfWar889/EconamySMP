package com.economysmp.crates;

import java.util.ArrayList;
import java.util.List;

public class CrateReward {
    public String rewardId;
    public String itemId;
    public int quantity;
    public String rarity; // COMMON, RARE, EPIC, LEGENDARY, MYTHIC
    public String displayName;
    public String description;

    public CrateReward(String rewardId, String itemId, int quantity, String rarity, String displayName, String description) {
        this.rewardId = rewardId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.rarity = rarity;
        this.displayName = displayName;
        this.description = description;
    }

    public static List<CrateReward> getCommonRewards() {
        List<CrateReward> rewards = new ArrayList<>();
        rewards.add(new CrateReward("common_1", "diamond", 1, "COMMON", "§bDiamond", "A shiny blue gem"));
        rewards.add(new CrateReward("common_2", "gold_ingot", 5, "COMMON", "§6Gold Ingots", "Precious metal"));
        rewards.add(new CrateReward("common_3", "emerald", 2, "COMMON", "§2Emeralds", "Green gems"));
        rewards.add(new CrateReward("common_4", "iron_ingot", 10, "COMMON", "§7Iron Ingots", "Sturdy metal"));
        rewards.add(new CrateReward("common_5", "coins", 100, "COMMON", "§e100 Coins", "Money!"));
        return rewards;
    }

    public static List<CrateReward> getRareRewards() {
        List<CrateReward> rewards = new ArrayList<>();
        rewards.add(new CrateReward("rare_1", "diamond", 5, "RARE", "§b5 Diamonds", "Lucky find!"));
        rewards.add(new CrateReward("rare_2", "gold_ingot", 15, "RARE", "§615 Gold Ingots", "Treasure!"));
        rewards.add(new CrateReward("rare_3", "emerald", 8, "RARE", "§28 Emeralds", "Green fortune"));
        rewards.add(new CrateReward("rare_4", "coins", 500, "RARE", "§e500 Coins", "Nice payout!"));
        rewards.add(new CrateReward("rare_5", "iron_pickaxe", 1, "RARE", "§7Iron Pickaxe", "Mining tool"));
        return rewards;
    }

    public static List<CrateReward> getEpicRewards() {
        List<CrateReward> rewards = new ArrayList<>();
        rewards.add(new CrateReward("epic_1", "diamond", 15, "EPIC", "§b15 Diamonds", "Excellent haul!"));
        rewards.add(new CrateReward("epic_2", "gold_ingot", 40, "EPIC", "§640 Gold Ingots", "Epic treasure"));
        rewards.add(new CrateReward("epic_3", "emerald", 20, "EPIC", "§220 Emeralds", "Lucky jackpot"));
        rewards.add(new CrateReward("epic_4", "coins", 2000, "EPIC", "§e2000 Coins", "Big payout!"));
        rewards.add(new CrateReward("epic_5", "diamond_pickaxe", 1, "EPIC", "§dDiamond Pickaxe", "Legendary tool"));
        return rewards;
    }

    public static List<CrateReward> getLegendaryRewards() {
        List<CrateReward> rewards = new ArrayList<>();
        rewards.add(new CrateReward("legendary_1", "diamond", 50, "LEGENDARY", "§b50 Diamonds", "Legendary riches!"));
        rewards.add(new CrateReward("legendary_2", "gold_ingot", 100, "LEGENDARY", "§6100 Gold Ingots", "Golden fortune"));
        rewards.add(new CrateReward("legendary_3", "emerald", 64, "LEGENDARY", "§264 Emeralds", "Emerald paradise"));
        rewards.add(new CrateReward("legendary_4", "coins", 5000, "LEGENDARY", "§e5000 Coins", "Massive payout!"));
        rewards.add(new CrateReward("legendary_5", "shards", 100, "LEGENDARY", "§d100 Shards", "Premium currency!"));
        return rewards;
    }

    public static List<CrateReward> getMythicRewards() {
        List<CrateReward> rewards = new ArrayList<>();
        rewards.add(new CrateReward("mythic_1", "diamond", 100, "MYTHIC", "§b100 Diamonds", "Mythical wealth!"));
        rewards.add(new CrateReward("mythic_2", "gold_ingot", 200, "MYTHIC", "§6200 Gold Ingots", "Unbelievable!"));
        rewards.add(new CrateReward("mythic_3", "emerald", 128, "MYTHIC", "§2128 Emeralds", "Emerald ocean"));
        rewards.add(new CrateReward("mythic_4", "coins", 10000, "MYTHIC", "§e10000 Coins", "Ultimate payout!"));
        rewards.add(new CrateReward("mythic_5", "shards", 500, "MYTHIC", "§d500 Shards", "Ultimate prize!"));
        return rewards;
    }
}

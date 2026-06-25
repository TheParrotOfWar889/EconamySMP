package com.economysmp.crates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CrateManager {
    private static final Random random = new Random();
    private static final Map<String, Long> crateOpenCooldown = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 5000; // 5 seconds

    public static CrateReward getRandomReward(String crateType) {
        List<CrateReward> rewards;
        
        switch (crateType.toLowerCase()) {
            case "common":
                rewards = CrateReward.getCommonRewards();
                break;
            case "rare":
                rewards = CrateReward.getRareRewards();
                break;
            case "epic":
                rewards = CrateReward.getEpicRewards();
                break;
            case "legendary":
                rewards = CrateReward.getLegendaryRewards();
                break;
            case "mythic":
                rewards = CrateReward.getMythicRewards();
                break;
            default:
                rewards = CrateReward.getCommonRewards();
        }
        
        return rewards.get(random.nextInt(rewards.size()));
    }

    public static long getShardCost(String crateType) {
        switch (crateType.toLowerCase()) {
            case "common":
                return 10;
            case "rare":
                return 25;
            case "epic":
                return 50;
            case "legendary":
                return 100;
            case "mythic":
                return 250;
            default:
                return 10;
        }
    }

    public static String getCrateColor(String crateType) {
        switch (crateType.toLowerCase()) {
            case "common":
                return "§f";
            case "rare":
                return "§b";
            case "epic":
                return "§d";
            case "legendary":
                return "§6";
            case "mythic":
                return "§c";
            default:
                return "§f";
        }
    }

    public static boolean canOpenCrate(String playerId) {
        Long lastOpen = crateOpenCooldown.get(playerId);
        if (lastOpen == null) {
            return true;
        }
        return System.currentTimeMillis() - lastOpen >= COOLDOWN_MILLIS;
    }

    public static void setCrateOpenCooldown(String playerId) {
        crateOpenCooldown.put(playerId, System.currentTimeMillis());
    }

    public static String getRewardDisplay(CrateReward reward) {
        String rarity = reward.rarity;
        String color = "";
        
        switch (rarity) {
            case "COMMON":
                color = "§f";
                break;
            case "RARE":
                color = "§b";
                break;
            case "EPIC":
                color = "§d";
                break;
            case "LEGENDARY":
                color = "§6";
                break;
            case "MYTHIC":
                color = "§c";
                break;
        }
        
        return color + "[" + rarity + "] " + reward.displayName + " §8" + reward.description;
    }
}

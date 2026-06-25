package com.economysmp.homes;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class HomeManager {
    private static final Map<UUID, Map<String, Home>> playerHomes = new HashMap<>();
    private static final int MAX_HOMES = 6;

    public static boolean setHome(UUID playerUuid, String homeName, double x, double y, double z, String world, float yaw, float pitch) {
        Map<String, Home> homes = playerHomes.computeIfAbsent(playerUuid, k -> new HashMap<>());
        
        if (homes.size() >= MAX_HOMES && !homes.containsKey(homeName)) {
            return false;
        }

        homes.put(homeName, new Home(homeName, x, y, z, world, yaw, pitch));
        return true;
    }

    public static Home getHome(UUID playerUuid, String homeName) {
        Map<String, Home> homes = playerHomes.get(playerUuid);
        return homes != null ? homes.get(homeName) : null;
    }

    public static List<Home> getPlayerHomes(UUID playerUuid) {
        Map<String, Home> homes = playerHomes.get(playerUuid);
        return homes != null ? new ArrayList<>(homes.values()) : new ArrayList<>();
    }

    public static boolean deleteHome(UUID playerUuid, String homeName) {
        Map<String, Home> homes = playerHomes.get(playerUuid);
        if (homes != null && homes.containsKey(homeName)) {
            homes.remove(homeName);
            return true;
        }
        return false;
    }

    public static int getHomeCount(UUID playerUuid) {
        Map<String, Home> homes = playerHomes.get(playerUuid);
        return homes != null ? homes.size() : 0;
    }
}

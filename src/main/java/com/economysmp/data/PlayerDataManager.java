package com.economysmp.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import com.economysmp.EconomySMPMod;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static MinecraftServer server;
    private static final Map<UUID, PlayerData> playerDataCache = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File dataFolder;

    public static void init(MinecraftServer minecraftServer) {
        server = minecraftServer;
        File worldFolder = server.getWorldDirectory().toFile();
        dataFolder = new File(worldFolder, "economysmp_data");
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            EconomySMPMod.LOGGER.info("Created EconomySMP data folder");
        }
        
        loadAllPlayerData();
    }

    private static void loadAllPlayerData() {
        if (dataFolder.listFiles() == null) return;
        
        for (File file : dataFolder.listFiles()) {
            if (file.getName().endsWith(".json")) {
                try {
                    FileReader reader = new FileReader(file);
                    PlayerData data = gson.fromJson(reader, PlayerData.class);
                    reader.close();
                    
                    if (data != null) {
                        playerDataCache.put(data.uuid, data);
                    }
                } catch (IOException e) {
                    EconomySMPMod.LOGGER.warn("Failed to load player data from " + file.getName(), e);
                }
            }
        }
        
        EconomySMPMod.LOGGER.info("Loaded " + playerDataCache.size() + " player records");
    }

    public static PlayerData getOrCreatePlayerData(UUID uuid) {
        return playerDataCache.computeIfAbsent(uuid, key -> {
            PlayerData newData = new PlayerData();
            newData.uuid = uuid;
            newData.coins = 0;
            newData.shards = 0;
            newData.level = 1;
            newData.xp = 0;
            newData.bankBalance = 0;
            newData.miningLevel = 1;
            newData.combatLevel = 1;
            newData.fishingLevel = 1;
            newData.farmingLevel = 1;
            newData.woodcuttingLevel = 1;
            return newData;
        });
    }

    public static void savePlayerData(UUID uuid) {
        PlayerData data = playerDataCache.get(uuid);
        if (data == null) return;
        
        try {
            File playerFile = new File(dataFolder, uuid + ".json");
            FileWriter writer = new FileWriter(playerFile);
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            EconomySMPMod.LOGGER.error("Failed to save player data for " + uuid, e);
        }
    }

    public static void saveAllPlayerData() {
        for (UUID uuid : playerDataCache.keySet()) {
            savePlayerData(uuid);
        }
    }

    public static long getPlayerCoins(UUID uuid) {
        return getOrCreatePlayerData(uuid).coins;
    }

    public static void addCoins(UUID uuid, long amount) {
        PlayerData data = getOrCreatePlayerData(uuid);
        data.coins += amount;
        savePlayerData(uuid);
    }

    public static void removeCoins(UUID uuid, long amount) {
        PlayerData data = getOrCreatePlayerData(uuid);
        if (data.coins >= amount) {
            data.coins -= amount;
            savePlayerData(uuid);
        }
    }

    public static long getPlayerShards(UUID uuid) {
        return getOrCreatePlayerData(uuid).shards;
    }

    public static void addShards(UUID uuid, long amount) {
        PlayerData data = getOrCreatePlayerData(uuid);
        data.shards += amount;
        savePlayerData(uuid);
    }

    public static void removeShards(UUID uuid, long amount) {
        PlayerData data = getOrCreatePlayerData(uuid);
        if (data.shards >= amount) {
            data.shards -= amount;
            savePlayerData(uuid);
        }
    }

    public static long getPlayerBankBalance(UUID uuid) {
        return getOrCreatePlayerData(uuid).bankBalance;
    }

    public static void setBankBalance(UUID uuid, long amount) {
        PlayerData data = getOrCreatePlayerData(uuid);
        data.bankBalance = amount;
        savePlayerData(uuid);
    }
}

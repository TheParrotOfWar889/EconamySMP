package com.economysmp.data;

import java.util.UUID;

public class PlayerData {
    public UUID uuid;
    public long coins = 0;
    public long shards = 0;
    public int level = 1;
    public long xp = 0;
    public long bankBalance = 0;
    
    // Skill Levels
    public int miningLevel = 1;
    public int combatLevel = 1;
    public int fishingLevel = 1;
    public int farmingLevel = 1;
    public int woodcuttingLevel = 1;
    
    // Homes
    public String[] homes = new String[5]; // Max 5 homes
}

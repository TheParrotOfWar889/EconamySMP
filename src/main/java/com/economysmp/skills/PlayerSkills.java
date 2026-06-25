package com.economysmp.skills;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class PlayerSkills {
    public UUID playerUuid;
    public Map<String, Skill> skills;

    public PlayerSkills(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.skills = new HashMap<>();
        initializeSkills();
    }

    private void initializeSkills() {
        skills.put("mining", new Skill("Mining", 0, 0));
        skills.put("combat", new Skill("Combat", 0, 0));
        skills.put("fishing", new Skill("Fishing", 0, 0));
        skills.put("farming", new Skill("Farming", 0, 0));
        skills.put("woodcutting", new Skill("Woodcutting", 0, 0));
    }

    public void addXp(String skillName, long xp) {
        Skill skill = skills.get(skillName.toLowerCase());
        if (skill != null) {
            skill.addXp(xp);
        }
    }

    public Skill getSkill(String skillName) {
        return skills.get(skillName.toLowerCase());
    }

    public static class Skill {
        public String name;
        public int level;
        public long experience;
        public static final long XP_PER_LEVEL = 1000;

        public Skill(String name, int level, long experience) {
            this.name = name;
            this.level = level;
            this.experience = experience;
        }

        public void addXp(long xp) {
            this.experience += xp;
            while (this.experience >= XP_PER_LEVEL) {
                this.experience -= XP_PER_LEVEL;
                this.level++;
            }
        }

        public long getXpToNextLevel() {
            return XP_PER_LEVEL - this.experience;
        }

        public double getProgressPercentage() {
            return (double) this.experience / XP_PER_LEVEL * 100;
        }
    }
}

package com.economysmp.shop;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ShopManager {
    private static final Map<String, ShopItem> shopItems = new HashMap<>();
    private static final List<String> categories = new ArrayList<>();

    static {
        initializeCategories();
        initializeShopItems();
    }

    private static void initializeCategories() {
        categories.add("Blocks");
        categories.add("Tools");
        categories.add("Armor");
        categories.add("Food");
        categories.add("Materials");
        categories.add("Decorations");
    }

    private static void initializeShopItems() {
        // Blocks
        addShopItem("dirt", new ItemStack(Items.DIRT), 5, 2, "Blocks");
        addShopItem("stone", new ItemStack(Items.STONE), 10, 5, "Blocks");
        addShopItem("oak_log", new ItemStack(Items.OAK_LOG), 15, 8, "Blocks");
        addShopItem("oak_planks", new ItemStack(Items.OAK_PLANKS), 5, 2, "Blocks");
        addShopItem("glass", new ItemStack(Items.GLASS), 25, 12, "Blocks");
        addShopItem("sand", new ItemStack(Items.SAND), 8, 4, "Blocks");
        addShopItem("gravel", new ItemStack(Items.GRAVEL), 10, 5, "Blocks");
        addShopItem("bricks", new ItemStack(Items.BRICK), 20, 10, "Blocks");

        // Tools
        addShopItem("wooden_pickaxe", new ItemStack(Items.WOODEN_PICKAXE), 50, 20, "Tools");
        addShopItem("stone_pickaxe", new ItemStack(Items.STONE_PICKAXE), 150, 75, "Tools");
        addShopItem("iron_pickaxe", new ItemStack(Items.IRON_PICKAXE), 500, 250, "Tools");
        addShopItem("diamond_pickaxe", new ItemStack(Items.DIAMOND_PICKAXE), 2000, 1000, "Tools");
        addShopItem("wooden_axe", new ItemStack(Items.WOODEN_AXE), 50, 20, "Tools");
        addShopItem("iron_axe", new ItemStack(Items.IRON_AXE), 400, 200, "Tools");
        addShopItem("diamond_axe", new ItemStack(Items.DIAMOND_AXE), 1800, 900, "Tools");

        // Armor
        addShopItem("iron_helmet", new ItemStack(Items.IRON_HELMET), 400, 200, "Armor");
        addShopItem("iron_chestplate", new ItemStack(Items.IRON_CHESTPLATE), 600, 300, "Armor");
        addShopItem("iron_leggings", new ItemStack(Items.IRON_LEGGINGS), 550, 275, "Armor");
        addShopItem("iron_boots", new ItemStack(Items.IRON_BOOTS), 300, 150, "Armor");
        addShopItem("diamond_helmet", new ItemStack(Items.DIAMOND_HELMET), 1500, 750, "Armor");
        addShopItem("diamond_chestplate", new ItemStack(Items.DIAMOND_CHESTPLATE), 2000, 1000, "Armor");

        // Food
        addShopItem("apple", new ItemStack(Items.APPLE), 50, 25, "Food");
        addShopItem("bread", new ItemStack(Items.BREAD), 30, 15, "Food");
        addShopItem("cooked_beef", new ItemStack(Items.COOKED_BEEF), 60, 30, "Food");
        addShopItem("cooked_chicken", new ItemStack(Items.COOKED_CHICKEN), 50, 25, "Food");
        addShopItem("cooked_salmon", new ItemStack(Items.COOKED_SALMON), 70, 35, "Food");
        addShopItem("golden_apple", new ItemStack(Items.GOLDEN_APPLE), 500, 250, "Food");

        // Materials
        addShopItem("iron_ore", new ItemStack(Items.IRON_ORE), 200, 100, "Materials");
        addShopItem("gold_ore", new ItemStack(Items.GOLD_ORE), 300, 150, "Materials");
        addShopItem("diamond_ore", new ItemStack(Items.DIAMOND_ORE), 1000, 500, "Materials");
        addShopItem("coal", new ItemStack(Items.COAL), 50, 25, "Materials");
        addShopItem("iron_ingot", new ItemStack(Items.IRON_INGOT), 100, 50, "Materials");
        addShopItem("gold_ingot", new ItemStack(Items.GOLD_INGOT), 200, 100, "Materials");
        addShopItem("diamond", new ItemStack(Items.DIAMOND), 500, 250, "Materials");

        // Decorations
        addShopItem("flower_pot", new ItemStack(Items.FLOWER_POT), 30, 15, "Decorations");
        addShopItem("bookshelf", new ItemStack(Items.BOOKSHELF), 80, 40, "Decorations");
        addShopItem("crafting_table", new ItemStack(Items.CRAFTING_TABLE), 100, 50, "Decorations");
        addShopItem("furnace", new ItemStack(Items.FURNACE), 150, 75, "Decorations");
    }

    private static void addShopItem(String id, ItemStack stack, long buyPrice, long sellPrice, String category) {
        ShopItem item = new ShopItem(id, stack, buyPrice, sellPrice, category, 64);
        shopItems.put(id, item);
    }

    public static ShopItem getShopItem(String itemId) {
        return shopItems.get(itemId);
    }

    public static List<ShopItem> getShopItemsByCategory(String category) {
        List<ShopItem> items = new ArrayList<>();
        for (ShopItem item : shopItems.values()) {
            if (item.category.equals(category)) {
                items.add(item);
            }
        }
        return items;
    }

    public static List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    public static List<ShopItem> getAllItems() {
        return new ArrayList<>(shopItems.values());
    }

    public static List<ShopItem> searchItems(String query) {
        List<ShopItem> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (ShopItem item : shopItems.values()) {
            if (item.itemId.toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }
        return results;
    }
}

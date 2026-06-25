package com.economysmp.shop;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.HashMap;
import java.util.Map;

public class ShopItem {
    public String itemId;
    public ItemStack itemStack;
    public long buyPrice;
    public long sellPrice;
    public String category;
    public int maxStack;

    public ShopItem(String itemId, ItemStack itemStack, long buyPrice, long sellPrice, String category, int maxStack) {
        this.itemId = itemId;
        this.itemStack = itemStack;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.category = category;
        this.maxStack = maxStack;
    }
}

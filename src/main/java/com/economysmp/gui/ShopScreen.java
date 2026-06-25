package com.economysmp.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.economysmp.shop.ShopManager;
import com.economysmp.shop.ShopItem;
import com.economysmp.data.PlayerDataManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.List;
import java.util.UUID;

public class ShopScreen extends Screen {
    private String currentCategory = "Blocks";
    private List<ShopItem> displayedItems;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_ROW = 5;
    private static final int ROWS_PER_PAGE = 3;
    private static final int ITEM_SIZE = 30;
    private static final int PADDING = 10;

    public ShopScreen(Text title) {
        super(title);
        this.displayedItems = ShopManager.getShopItemsByCategory(currentCategory);
    }

    @Override
    protected void init() {
        super.init();
        this.clearChildren();

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Back"),
            button -> this.close()
        ).dimensions(this.width - 110, 10, 100, 20).build());

        // Category Buttons
        int categoryButtonY = 40;
        int categoryButtonX = 10;
        int categoryIndex = 0;
        
        for (String category : ShopManager.getCategories()) {
            final String cat = category;
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(cat),
                button -> changeCategory(cat)
            ).dimensions(categoryButtonX + (categoryIndex * 110), categoryButtonY, 100, 20).build());
            categoryIndex++;
        }

        // Search Button (placeholder for now)
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Search"),
            button -> {
                // Search functionality would go here
            }
        ).dimensions(10, this.height - 30, 100, 20).build());

        // Sell Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Sell Items"),
            button -> this.client.setScreen(new SellScreen(Text.literal("Sell Items")))
        ).dimensions(this.width - 110, this.height - 30, 100, 20).build());
    }

    private void changeCategory(String category) {
        this.currentCategory = category;
        this.displayedItems = ShopManager.getShopItemsByCategory(category);
        this.scrollOffset = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.literal("§6§lShop - " + currentCategory), 
            this.width / 2, 15, 0xFFFFFF);

        // Display items
        int itemX = PADDING;
        int itemY = 80;
        int itemsShown = 0;
        int maxItems = ITEMS_PER_ROW * ROWS_PER_PAGE;

        for (int i = scrollOffset; i < displayedItems.size() && itemsShown < maxItems; i++) {
            ShopItem item = displayedItems.get(i);
            
            // Draw item box
            int boxX = itemX + (itemsShown % ITEMS_PER_ROW) * (ITEM_SIZE + 20);
            int boxY = itemY + (itemsShown / ITEMS_PER_ROW) * (ITEM_SIZE + 50);

            // Background
            context.fill(boxX, boxY, boxX + ITEM_SIZE + 10, boxY + ITEM_SIZE + 40, 0xFF2B2B2B);
            
            // Border
            context.drawBorder(boxX, boxY, ITEM_SIZE + 10, ITEM_SIZE + 40, 0xFF00FF00);

            // Item name
            context.drawTextWithShadow(this.textRenderer, 
                Text.literal(item.itemId), 
                boxX + 5, boxY + 5, 0xFFFFFF);

            // Buy price
            context.drawTextWithShadow(this.textRenderer, 
                Text.literal("§aB: " + item.buyPrice), 
                boxX + 5, boxY + 20, 0x00FF00);

            // Sell price
            context.drawTextWithShadow(this.textRenderer, 
                Text.literal("§cS: " + item.sellPrice), 
                boxX + 5, boxY + 30, 0xFF0000);

            itemsShown++;
        }

        // Scroll info
        context.drawTextWithShadow(this.textRenderer,
            Text.literal("Page: " + (scrollOffset / maxItems + 1)),
            PADDING, this.height - 50, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int maxPages = (displayedItems.size() - 1) / (ITEMS_PER_ROW * ROWS_PER_PAGE) + 1;
        int maxScroll = Math.max(0, (maxPages - 1) * ITEMS_PER_ROW * ROWS_PER_PAGE);
        
        if (verticalAmount > 0) {
            scrollOffset = Math.max(0, scrollOffset - (ITEMS_PER_ROW * ROWS_PER_PAGE));
        } else {
            scrollOffset = Math.min(maxScroll, scrollOffset + (ITEMS_PER_ROW * ROWS_PER_PAGE));
        }
        return true;
    }
}

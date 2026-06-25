package com.economysmp.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.economysmp.shop.ShopManager;
import com.economysmp.shop.ShopItem;
import com.economysmp.data.PlayerDataManager;
import net.minecraft.client.MinecraftClient;
import java.util.List;
import java.util.UUID;

public class SellScreen extends Screen {
    private List<ShopItem> sellableItems;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_ROW = 5;
    private static final int ROWS_PER_PAGE = 3;
    private static final int ITEM_SIZE = 30;
    private static final int PADDING = 10;

    public SellScreen(Text title) {
        super(title);
        this.sellableItems = ShopManager.getAllItems();
    }

    @Override
    protected void init() {
        super.init();
        this.clearChildren();

        // Back to Shop Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Back to Shop"),
            button -> this.client.setScreen(new ShopScreen(Text.literal("Shop")))
        ).dimensions(this.width - 130, 10, 120, 20).build());

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            button -> this.close()
        ).dimensions(this.width - 10 - 50, 10, 50, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.literal("§c§lSell Items"), 
            this.width / 2, 15, 0xFFFFFF);

        // Display sellable items
        int itemX = PADDING;
        int itemY = 50;
        int itemsShown = 0;
        int maxItems = ITEMS_PER_ROW * ROWS_PER_PAGE;

        for (int i = scrollOffset; i < sellableItems.size() && itemsShown < maxItems; i++) {
            ShopItem item = sellableItems.get(i);
            
            int boxX = itemX + (itemsShown % ITEMS_PER_ROW) * (ITEM_SIZE + 20);
            int boxY = itemY + (itemsShown / ITEMS_PER_ROW) * (ITEM_SIZE + 50);

            // Background
            context.fill(boxX, boxY, boxX + ITEM_SIZE + 10, boxY + ITEM_SIZE + 40, 0xFF1A1A1A);
            
            // Border
            context.drawBorder(boxX, boxY, ITEM_SIZE + 10, ITEM_SIZE + 40, 0xFFFF0000);

            // Item name
            context.drawTextWithShadow(this.textRenderer, 
                Text.literal(item.itemId), 
                boxX + 5, boxY + 5, 0xFFFFFF);

            // Sell price
            context.drawTextWithShadow(this.textRenderer, 
                Text.literal("§a" + item.sellPrice + " coins"), 
                boxX + 5, boxY + 25, 0x00FF00);

            itemsShown++;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int maxScroll = Math.max(0, sellableItems.size() - (ITEMS_PER_ROW * ROWS_PER_PAGE));
        
        if (verticalAmount > 0) {
            scrollOffset = Math.max(0, scrollOffset - ITEMS_PER_ROW);
        } else {
            scrollOffset = Math.min(maxScroll, scrollOffset + ITEMS_PER_ROW);
        }
        return true;
    }
}

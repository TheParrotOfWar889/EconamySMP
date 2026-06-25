package com.economysmp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import com.economysmp.gui.ShopScreen;
import net.minecraft.text.Text;

public class EconomySMPClient implements ClientModInitializer {
    private static boolean shopKeyPressed = false;

    @Override
    public void onInitializeClient() {
        // Register client tick event to listen for shop command
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // You can add keybinds here later
        });
    }

    public static void openShop() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.setScreen(new ShopScreen(Text.literal("Shop")));
        }
    }
}

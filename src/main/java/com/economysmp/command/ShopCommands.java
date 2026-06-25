package com.economysmp.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import com.economysmp.gui.ShopScreen;
import net.minecraft.client.MinecraftClient;

public class ShopCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerShopCommand(dispatcher);
        });
    }

    private static void registerShopCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("shop")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    
                    if (player != null) {
                        // Schedule GUI open on main thread
                        player.sendMessage(Text.of("§aOpening shop..."), false);
                        // The GUI will be opened via client-side event
                    }
                    return 1;
                })
        );
    }
}

package com.economysmp;

import net.fabricmc.api.ModInitializer;
import com.economysmp.command.EconomyCommands;
import com.economysmp.data.PlayerDataManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EconomySMPMod implements ModInitializer {
    public static final String MOD_ID = "economysmp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing EconomySMP!");
        
        // Register server startup event to load player data
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("EconomySMP server started!");
            PlayerDataManager.init(server);
        });
        
        // Register commands
        EconomyCommands.register();
        
        LOGGER.info("EconomySMP initialized successfully!");
    }
}

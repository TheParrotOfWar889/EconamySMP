package com.economysmp;

import net.fabricmc.api.ModInitializer;
import com.economysmp.command.EconomyCommands;
import com.economysmp.command.ShopCommands;
import com.economysmp.command.MarketplaceCommands;
import com.economysmp.data.PlayerDataManager;
import com.economysmp.marketplace.MarketplaceManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EconomySMPMod implements ModInitializer {
    public static final String MOD_ID = "economysmp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing EconomySMP!");
        
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("EconomySMP server started!");
            PlayerDataManager.init(server);
            MarketplaceManager.init(server);
        });
        
        EconomyCommands.register();
        ShopCommands.register();
        MarketplaceCommands.register();
        
        LOGGER.info("EconomySMP initialized successfully!");
    }
}

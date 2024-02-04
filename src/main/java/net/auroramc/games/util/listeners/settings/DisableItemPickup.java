/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.player.PlayerPickupItemEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisableItemPickup implements Listener {

    private final static DisableItemPickup instance;

    static {
        instance = new DisableItemPickup();
    }

    @EventHandler
    public void onMove(PlayerPickupItemEvent e) {
        e.setCancelled(!EngineAPI.getActiveGame().isItemPickup());
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerPickupItemEvent.getHandlerList().unregister(instance);
    }
}

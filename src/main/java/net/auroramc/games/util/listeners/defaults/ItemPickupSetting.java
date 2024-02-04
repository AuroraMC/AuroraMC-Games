/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerPickupItemEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ItemPickupSetting implements Listener {

    private final static ItemPickupSetting instance;

    static {
        instance = new ItemPickupSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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

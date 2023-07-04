/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

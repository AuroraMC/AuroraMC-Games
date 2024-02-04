/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisableItemDrop implements Listener {

    private final static DisableItemDrop instance;

    static {
        instance = new DisableItemDrop();
    }

    @EventHandler
    public void onMove(PlayerDropItemEvent e) {
        e.setCancelled(!EngineAPI.getActiveGame().isItemDrop());
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerDropItemEvent.getHandlerList().unregister(instance);
    }
}

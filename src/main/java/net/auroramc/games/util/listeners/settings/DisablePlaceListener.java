/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisablePlaceListener implements Listener {

    private final static DisablePlaceListener instance;

    static {
        instance = new DisablePlaceListener();
    }

    @EventHandler
    public void onMove(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        BlockPlaceEvent.getHandlerList().unregister(instance);
    }
}
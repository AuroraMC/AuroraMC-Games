/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.block.BlockPlaceEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockPlaceSetting implements Listener {

    private final static BlockPlaceSetting instance;

    static {
        instance = new BlockPlaceSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockPlaceCreative() && !EngineAPI.getActiveGame().isBlockPlace());
        } else {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockPlace());
        }
    }


    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        BlockPlaceEvent.getHandlerList().unregister(instance);
    }
}

/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisableBreakListener implements Listener {

    private final static DisableBreakListener instance;

    static {
        instance = new DisableBreakListener();
    }

    @EventHandler
    public void onMove(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockBreak() && !EngineAPI.getActiveGame().isBlockBreakCreative());
        } else {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockBreak());
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        BlockBreakEvent.getHandlerList().unregister(instance);
    }
}

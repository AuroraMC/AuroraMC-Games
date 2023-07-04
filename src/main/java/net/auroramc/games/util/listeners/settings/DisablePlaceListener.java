/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.block.BlockPlaceEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisablePlaceListener implements Listener {

    private final static DisablePlaceListener instance;

    static {
        instance = new DisablePlaceListener();
    }

    @EventHandler
    public void onMove(BlockPlaceEvent e) {
        if (!EngineAPI.getActiveGame().isBlockPlace()) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE && EngineAPI.getActiveGame().isBlockPlaceCreative()) {
                return;
            }
            e.setCancelled(true);
        } else if (e.getPlayer().getGameMode() == GameMode.CREATIVE && !EngineAPI.getActiveGame().isBlockPlaceCreative()) {
            e.setCancelled(true);
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        BlockPlaceEvent.getHandlerList().unregister(instance);
    }
}

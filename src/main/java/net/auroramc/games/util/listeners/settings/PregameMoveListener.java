/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PregameMoveListener implements Listener {

    private final static PregameMoveListener instance;

    static {
        instance = new PregameMoveListener();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                e.setTo(e.getFrom());
            }
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerMoveEvent.getHandlerList().unregister(instance);
    }

}

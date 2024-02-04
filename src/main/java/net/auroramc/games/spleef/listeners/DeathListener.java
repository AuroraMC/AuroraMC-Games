/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerShow(PlayerShowEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            e.setHidden(true);
        }
    }
}

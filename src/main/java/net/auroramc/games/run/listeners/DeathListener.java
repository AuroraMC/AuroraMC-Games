/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.run.listeners;

import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerShow(PlayerShowEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            e.setHidden(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }
}

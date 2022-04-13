/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.run.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()) && EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
            Location location = e.getTo().clone();
            location.setY(location.getY() - 1);
            if (location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.STAINED_CLAY) {
                location.getBlock().setType(Material.STAINED_CLAY);
                location.getBlock().setData((byte)14);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        location.getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 20);
            }
        }
    }

}

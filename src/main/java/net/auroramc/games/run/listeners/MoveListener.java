/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.listeners;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        AuroraMCGamePlayer gp = (AuroraMCGamePlayer) e.getPlayer();
        if (gp.isSpectator() || gp.isVanished()) {
            return;
        }
        if (EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
            Location location = e.getTo().clone();
            location.setY(location.getY() - 1);
            if (location.getBlock().getType() != Material.AIR && (location.getBlock().getType() != Material.STAINED_CLAY || location.getBlock().getData() != 14) && !location.getBlock().isLiquid()) {
                gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                location.getBlock().setType(Material.STAINED_CLAY);
                location.getBlock().setData((byte)14);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        location.getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(ServerAPI.getCore(), 20);
            } else if (location.getBlock().isLiquid() && (location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER)) {
                EntityDamageEvent event = new EntityDamageEvent(e.getPlayer().getCraft(), EntityDamageEvent.DamageCause.VOID, 500);
                Bukkit.getPluginManager().callEvent(event);
                return;
            }
            int wholeX = (int) ((location.getX() >= 0)?Math.floor(location.getX()):Math.ceil(location.getX()));
            int wholeZ = (int) ((location.getZ() >= 0)?Math.floor(location.getZ()):Math.ceil(location.getZ()));
            double decX = Math.abs(location.getX() - wholeX);
            double decZ = Math.abs(location.getZ() - wholeZ);
            Location x = null;
            Location z = null;
            if (decX <= 0.31) {
                x = location.clone();
                if (x.getX() >= 0) {
                    x.setX(x.getX() - 1);
                } else {
                    x.setX(x.getX() + 1);
                }
            } else if (decX >= 0.69) {
                x = location.clone();
                if (x.getX() >= 0) {
                    x.setX(x.getX() + 1);
                } else {
                    x.setX(x.getX() - 1);
                }
            }
            if (x != null && x.getBlock().getType() != Material.AIR && (x.getBlock().getType() != Material.STAINED_CLAY || x.getBlock().getData() != 14) && !x.getBlock().isLiquid()) {
                gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                x.getBlock().setType(Material.STAINED_CLAY);
                x.getBlock().setData((byte)14);
                Location finX = x;
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        finX.getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(ServerAPI.getCore(), 20);
            }

            if (decZ <= 0.31) {
                z = location.clone();
                if (z.getZ() >= 0) {
                    z.setZ(z.getZ() - 1);
                } else {
                    z.setZ(z.getZ() + 1);
                }
            } else if (decZ >= 0.69) {
                z = location.clone();
                if (z.getZ() >= 0) {
                    z.setZ(z.getZ() + 1);
                } else {
                    z.setZ(z.getZ() - 1);
                }
            }
            if (z != null) {
                if (z.getBlock().getType() != Material.AIR && (z.getBlock().getType() != Material.STAINED_CLAY || z.getBlock().getData() != 14) && !z.getBlock().isLiquid()) {
                    gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                    z.getBlock().setType(Material.STAINED_CLAY);
                    z.getBlock().setData((byte)14);
                    Location finZ = z;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            finZ.getBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(ServerAPI.getCore(), 20);
                }
                if (x != null) {
                    Location loc2 = x.clone();
                    loc2.setZ(z.getZ());
                    if (loc2.getBlock().getType() != Material.AIR && (loc2.getBlock().getType() != Material.STAINED_CLAY || loc2.getBlock().getData() != 14) && !loc2.getBlock().isLiquid()) {
                        gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                        loc2.getBlock().setType(Material.STAINED_CLAY);
                        loc2.getBlock().setData((byte)14);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                loc2.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(ServerAPI.getCore(), 20);
                    }
                }
            }
        }
    }

}

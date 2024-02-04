/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class WaterListener implements Listener {

    @EventHandler
    public void onWater(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
            if (EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                AuroraMCServerPlayer player = e.getPlayer();
                if (player instanceof AuroraMCGamePlayer) {
                    if (!((AuroraMCGamePlayer) player).isSpectator() && !player.isVanished() && !player.isDead()) {
                        if (player.getLocation().getBlock().isLiquid() && (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
                            EntityDamageEvent event = new EntityDamageEvent(e.getPlayer().getCraft(), EntityDamageEvent.DamageCause.VOID, 500);
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    }
                }
            }
        }
    }

}

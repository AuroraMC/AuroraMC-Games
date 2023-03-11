/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class WaterListener implements Listener {

    @EventHandler
    public void onWater(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
            if (EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                if (player instanceof AuroraMCGamePlayer) {
                    if (!((AuroraMCGamePlayer) player).isSpectator() && !player.isVanished() && !player.isDead()) {
                        if (player.getPlayer().getLocation().getBlock().isLiquid() && (player.getPlayer().getLocation().getBlock().getType() == Material.WATER || player.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
                            EntityDamageEvent event = new EntityDamageEvent(e.getPlayer(), EntityDamageEvent.DamageCause.VOID, 500);
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    }
                }
            }
        }
    }

}

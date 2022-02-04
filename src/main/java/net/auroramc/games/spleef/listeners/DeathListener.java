/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCGamePlayer gamePlayer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getEntity().getUniqueId());
            if (!EngineAPI.getActiveGame().isStarting() && !gamePlayer.isSpectator()) {
                e.setDamage(0);
                if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    gamePlayer.setSpectator(true, true);
                    gamePlayer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You died."));
                    JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                    int x, y, z;
                    x = specSpawn.getInt("x");
                    y = specSpawn.getInt("y");
                    z = specSpawn.getInt("z");
                    float yaw = specSpawn.getFloat("yaw");
                    gamePlayer.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.hidePlayer(gamePlayer.getPlayer());
                    }
                    List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
                    if (playersAlive.size() == 1) {
                        EngineAPI.getActiveGame().end(playersAlive.get(0));
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

}

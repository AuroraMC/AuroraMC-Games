/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.death;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONArray;
import org.json.JSONObject;

public class BorderListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
            if (EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                if (!EngineAPI.getActiveMap().isInBorder(e.getTo())) {
                    //Call entity damage event so the games can handle them appropriately.
                    EntityDamageEvent event = new EntityDamageEvent(e.getPlayer(), EntityDamageEvent.DamageCause.VOID, 500);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else if (EngineAPI.getServerState() != ServerState.ENDING && EngineAPI.getServerState() != ServerState.IN_GAME) {
                if (!EngineAPI.getWaitingLobbyMap().isInBorder(e.getTo())) {
                    //Call entity damage event so the games can handle them appropriately.
                    JSONArray spawnLocations = EngineAPI.getWaitingLobbyMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
                    if (spawnLocations == null || spawnLocations.length() == 0) {
                        EngineAPI.getGameEngine().getLogger().info("An invalid waiting lobby was supplied, assuming 0, 64, 0 spawn position.");
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 64, 0));
                    } else {
                        int x, y, z;
                        x = spawnLocations.getJSONObject(0).getInt("x");
                        y = spawnLocations.getJSONObject(0).getInt("y");
                        z = spawnLocations.getJSONObject(0).getInt("z");
                        float yaw = spawnLocations.getJSONObject(0).getFloat("yaw");
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), x, y, z, yaw, 0));
                    }
                    if (EngineAPI.getWaitingLobbyMap().getMapData().getInt("time") > 12000) {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0, true, false), false);
                    }
                }
            }
        }
    }

}

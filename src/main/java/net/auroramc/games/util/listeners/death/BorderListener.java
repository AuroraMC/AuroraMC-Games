/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.death;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.json.JSONObject;

public class BorderListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
            if (EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                int highX = 0, lowX = 0, highY = 0, lowY = 0, highZ = 0, lowZ = 0;
                JSONObject a = EngineAPI.getActiveMap().getMapData().getJSONObject("border_a");
                JSONObject b = EngineAPI.getActiveMap().getMapData().getJSONObject("border_b");
                if (a.getInt("x") > b.getInt("x")) {
                    highX = a.getInt("x");
                    lowX = b.getInt("x");
                } else {
                    highX = b.getInt("x");
                    lowX = a.getInt("x");
                }

                if (a.getInt("y") > b.getInt("y")) {
                    highY = a.getInt("y");
                    lowY = b.getInt("y");
                } else {
                    highY = b.getInt("y");
                    lowY = a.getInt("y");
                }

                if (a.getInt("z") > b.getInt("z")) {
                    highZ = a.getInt("z");
                    lowZ = b.getInt("z");
                } else {
                    highZ = b.getInt("z");
                    lowZ = a.getInt("z");
                }

                if (e.getTo().getX() < lowX || e.getTo().getX() > highX || e.getTo().getY() < lowY || e.getTo().getY() > highY || e.getTo().getZ() < lowZ || e.getTo().getZ() > highZ) {
                    //Call entity damage event so the games can handle them appropriately.
                    EntityDamageEvent event = new EntityDamageEvent(e.getPlayer(), EntityDamageEvent.DamageCause.VOID, 500);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }
    }

}

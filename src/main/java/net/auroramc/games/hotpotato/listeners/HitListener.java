/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.paintball.teams.PBRed;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class HitListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamager() instanceof Player) {
            e.setDamage(0);
            AuroraMCPlayer pl = AuroraMCAPI.getPlayer((Player) e.getDamager());
            if (pl instanceof AuroraMCGamePlayer) {
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
                if (!player.isSpectator() && !player.isVanished()) {
                    AuroraMCGamePlayer newHolder = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
                    if (player.getGameData().containsKey("potato_holder") && !newHolder.getGameData().containsKey("potato_holder")) {
                        Potato potato = (Potato) player.getGameData().get("potato_holder");
                        potato.newHolder(newHolder);
                    } else if (player.getGameData().containsKey("potato_holder") && newHolder.getGameData().containsKey("potato_holder")) {
                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(166))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(166), 1, true);
                        }
                    } else if (!player.getGameData().containsKey("potato_holder")) {
                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(169))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(169), 1, true);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof EntityDamageByEntityEvent)) {
            e.setCancelled(true);
        }

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You went outside of the border so you were teleported back to spawn."));
            JSONArray playerSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = playerSpawns.getJSONObject(new Random().nextInt(playerSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }

    }
}

/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.hotpotato.entities.Potato;
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
    public void onEntityDamageByEntity(PlayerDamageByPlayerEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }

        if (!EngineAPI.getActiveGame().isDamagePvP()) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(0);
        AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getDamager();
        if (!player.isSpectator() && !player.isVanished()) {
            AuroraMCGamePlayer newHolder = (AuroraMCGamePlayer) e.getPlayer();
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
    }

    @EventHandler
    public void onEntityDamage(PlayerDamageEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof PlayerDamageByPlayerEvent)) {
            e.setCancelled(true);
            return;
        }

        if (!EngineAPI.getActiveGame().isDamageAll()) {
            e.setCancelled(true);
            return;
        }

        if (e.getCause() == PlayerDamageEvent.DamageCause.VOID) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            player.sendMessage(TextFormatter.pluginMessage("Game", "You went outside of the border so you were teleported back to spawn."));
            JSONArray playerSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = playerSpawns.getJSONObject(new Random().nextInt(playerSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }

    }
}

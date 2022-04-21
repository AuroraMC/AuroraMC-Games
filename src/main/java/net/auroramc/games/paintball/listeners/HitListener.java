/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class HitListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e instanceof EntityDamageByEntityEvent)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            Snowball snowball = (Snowball) e.getDamager();
            if (player.isSpectator() || player.isVanished()) {
                e.setCancelled(true);
                return;
            }

            AuroraMCPlayer shooter = null;
            Turret turret = null;
            if (snowball.getShooter() instanceof ArmorStand) {
                turret = ((Paintball)EngineAPI.getActiveGame()).getTurrets().get((ArmorStand) snowball.getShooter());
                if (turret.getOwner().getTeam().equals(player.getTeam())) {
                    e.setCancelled(true);
                    return;
                }
                if (turret.getOwner().getTeam() instanceof  PBRed) {
                    ((PBRed)turret.getOwner().getTeam()).addLife();
                    ((PBBlue)player.getTeam()).removeLife();
                    if (((PBBlue)player.getTeam()).getLives() == 0) {
                        EngineAPI.getActiveGame().end(turret.getOwner().getTeam(), null);
                    }
                } else {
                    ((PBBlue)turret.getOwner().getTeam()).addLife();
                    ((PBRed)player.getTeam()).removeLife();
                    if (((PBRed)player.getTeam()).getLives() == 0) {
                        EngineAPI.getActiveGame().end(turret.getOwner().getTeam(), null);
                    }
                }
            } else if (snowball.getShooter() instanceof Player) {
                shooter = AuroraMCAPI.getPlayer((Player) snowball.getShooter());
                if (shooter.getTeam().equals(player.getTeam())) {
                    e.setCancelled(true);
                    return;
                }
                if (shooter.getTeam() instanceof  PBRed) {
                    ((PBRed)shooter.getTeam()).addLife();
                    ((PBBlue)player.getTeam()).removeLife();
                    if (((PBBlue)player.getTeam()).getLives() == 0) {
                        EngineAPI.getActiveGame().end(shooter.getTeam(), null);
                    }
                } else {
                    ((PBBlue)shooter.getTeam()).addLife();
                    ((PBRed)player.getTeam()).removeLife();
                    if (((PBRed)player.getTeam()).getLives() == 0) {
                        EngineAPI.getActiveGame().end(shooter.getTeam(), null);
                    }
                }
                shooter.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);
            }
            e.setDamage(0);
            JSONArray redSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("RED");
            JSONArray blueSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("BLUE");
            if (player.getTeam() instanceof PBRed) {
                JSONObject spawn = redSpawns.getJSONObject(new Random().nextInt(redSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = blueSpawns.getJSONObject(new Random().nextInt(blueSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);
            if (shooter != null) {
                KillMessage killMessage = (KillMessage) shooter.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, shooter, player, null, KillMessage.KillReason.TAG)));
                }
            } else if (turret != null) {
                KillMessage killMessage = (KillMessage) turret.getOwner().getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, null, player, turret.getArmorStand(), KillMessage.KillReason.TAG)));
                }
            } else {
                KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, null, player, null, KillMessage.KillReason.TAG)));
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        e.setCancelled(true);
    }

}

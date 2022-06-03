/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.DeathEffect;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.kits.Tribute;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
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
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You went outside of the border so was teleported back to spawn."));
            JSONArray redSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("RED");
            JSONArray blueSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("BLUE");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
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
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.getDamager() instanceof Snowball) {
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
                AuroraMCGamePlayer gp = (AuroraMCGamePlayer) turret.getOwner();
                gp.getGameData().put("gold", (int)gp.getGameData().get("gold") + 2);
                int i = (int)gp.getGameData().get("gold");
                if (i == 0) {
                    i = 1;
                }
                gp.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gp.getGameData().get("gold") + " Gold", i).getItem());
                int amount = 1;
                if (gp.getKit() instanceof Tribute) {
                    if (gp.getKitLevel().getLatestUpgrade() == 2) {
                        amount = 2;
                    } else if (gp.getKitLevel().getLatestUpgrade() == 4) {
                        amount = 3;
                    } else if (gp.getKitLevel().getLatestUpgrade() == 5) {
                        amount = 4;
                    }
                }
                if (!gp.getPlayer().getInventory().contains(Material.SNOW_BALL, 64)) {
                    if (gp.getPlayer().getInventory().getItem(0) != null && gp.getPlayer().getInventory().getItem(0).getType() == Material.SNOW_BALL) {
                        if (gp.getPlayer().getInventory().getItem(0).getAmount() + amount > 64) {
                            amount = 64 - gp.getPlayer().getInventory().getItem(0).getAmount();
                        }
                        gp.getPlayer().getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, gp.getPlayer().getInventory().getItem(0).getAmount() + amount, null).getItem());
                    } else {
                        gp.getPlayer().getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, amount, null).getItem());
                    }
                }
                gp.getPlayer().sendMessage(AuroraMCAPI.getFormatter().convert("&6+2 Gold"));
                gp.getRewards().addXp("Kills", 25);
                if (turret.getOwner().getTeam() instanceof PBRed) {
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
                AuroraMCGamePlayer gp = (AuroraMCGamePlayer) shooter;
                gp.getGameData().put("gold", (int)gp.getGameData().get("gold") + 2);
                int i = (int)gp.getGameData().get("gold");
                if (i == 0) {
                    i = 1;
                }
                gp.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gp.getGameData().get("gold") + " Gold", i).getItem());
                int amount = 1;
                if (gp.getKit() instanceof Tribute) {
                    if (gp.getKitLevel().getLatestUpgrade() == 2) {
                        amount = 2;
                    } else if (gp.getKitLevel().getLatestUpgrade() == 4) {
                        amount = 3;
                    } else if (gp.getKitLevel().getLatestUpgrade() == 5) {
                        amount = 4;
                    }
                }
                if (!gp.getPlayer().getInventory().contains(Material.SNOW_BALL, 64)) {
                    if (gp.getPlayer().getInventory().getItem(0) != null && gp.getPlayer().getInventory().getItem(0).getType() == Material.SNOW_BALL) {
                        if (gp.getPlayer().getInventory().getItem(0).getAmount() + amount > 64) {
                            amount = 64 - gp.getPlayer().getInventory().getItem(0).getAmount();
                        }
                        gp.getPlayer().getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, gp.getPlayer().getInventory().getItem(0).getAmount() + amount, null).getItem());
                    } else {
                        gp.getPlayer().getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, amount, null).getItem());
                    }
                }
                gp.getPlayer().sendMessage(AuroraMCAPI.getFormatter().convert("&6+2 Gold"));
                gp.getRewards().addXp("Kills", 25);
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
            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT)) {
                ((DeathEffect)player.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT)).onDeath(player);
            }
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
            player.getKit().onGameStart(player);
            if (shooter != null) {
                KillMessage killMessage = (KillMessage) shooter.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, shooter, player, null, KillMessage.KillReason.PAINTBALL, EngineAPI.getActiveGameInfo().getId())));
                }
            } else if (turret != null) {
                KillMessage killMessage = (KillMessage) turret.getOwner().getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, turret.getOwner(), player, turret.getArmorStand(), KillMessage.KillReason.PAINTBALL, EngineAPI.getActiveGameInfo().getId())));
                }
            } else {
                KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                    player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, null, player, null, KillMessage.KillReason.PAINTBALL, EngineAPI.getActiveGameInfo().getId())));
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

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity().getShooter());
            gp.getKit().onGameStart(gp);
        }
    }

}

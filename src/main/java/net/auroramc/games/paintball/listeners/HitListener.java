/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.paintball.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByEntityEvent;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerRangedEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.events.player.PlayerArmorStandManipulateEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.kits.Tribute;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class HitListener implements Listener {



    @EventHandler
    public void onDamage(PlayerDamageEvent e) {
        if (!EngineAPI.getActiveGame().isDamageAll()) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof PlayerDamageByPlayerEvent)) {
            e.setCancelled(true);
        }

        if (e.getCause() == PlayerDamageEvent.DamageCause.VOID) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.DEATH, new JSONObject().put("player", player.getName()).put("killer", "None").put("final", false)));
            player.sendMessage(TextFormatter.pluginMessage("Game", "You went outside of the border so was teleported back to spawn."));
            JSONArray redSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("RED");
            JSONArray blueSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("BLUE");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                if (player.getTeam() instanceof PBRed) {
                    JSONObject spawn = redSpawns.getJSONObject(new Random().nextInt(redSpawns.length()));
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                } else {
                    JSONObject spawn = blueSpawns.getJSONObject(new Random().nextInt(blueSpawns.length()));
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

    @EventHandler
    public void onDamage(PlayerDamageByPlayerEvent e) {
        if (!EngineAPI.getActiveGame().isDamagePvP()) {
            e.setCancelled(true);
            return;
        }
        if (e instanceof PlayerDamageByPlayerRangedEvent) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (player.isSpectator() || player.isVanished()) {
                e.setCancelled(true);
                return;
            }

            AuroraMCServerPlayer shooter = null;
            shooter = e.getDamager();
            if (shooter.getTeam().equals(player.getTeam())) {
                e.setCancelled(true);
                return;
            }
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) shooter;
            gp.getGameData().put("gold", (int) gp.getGameData().get("gold") + 2);
            int i = (int) gp.getGameData().get("gold");
            if (i == 0) {
                i = 1;
            }
            gp.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gp.getGameData().get("gold") + " Gold", i).getItemStack());
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
            if (!gp.getInventory().contains(Material.SNOW_BALL, 64)) {
                if (gp.getInventory().getItem(0) != null && gp.getInventory().getItem(0).getType() == Material.SNOW_BALL) {
                    if (gp.getInventory().getItem(0).getAmount() + amount > 64) {
                        amount = 64 - gp.getInventory().getItem(0).getAmount();
                    }
                    gp.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, gp.getInventory().getItem(0).getAmount() + amount, null).getItemStack());
                } else {
                    gp.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, amount, null).getItemStack());
                }
            }
            gp.sendMessage(new TextComponent(TextFormatter.convert("&6+2 Gold")));
            gp.getRewards().addXp("Kills", 25);
            shooter.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);

            if (player.getTeam() instanceof PBRed) {
                ((PBBlue) EngineAPI.getActiveGame().getTeams().get("Blue")).addLife();
                ((PBRed) player.getTeam()).removeLife();
                if (((PBRed) player.getTeam()).getLives() <= 0) {
                    EngineAPI.getActiveGame().end(EngineAPI.getActiveGame().getTeams().get("Blue"), null);
                }
            } else {
                ((PBRed) EngineAPI.getActiveGame().getTeams().get("Red")).addLife();
                ((PBBlue) player.getTeam()).removeLife();
                if (((PBBlue) player.getTeam()).getLives() <= 0) {
                    EngineAPI.getActiveGame().end(EngineAPI.getActiveGame().getTeams().get("Red"), null);
                }
            }
            e.setDamage(0);
            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT)) {
                ((DeathEffect) player.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT)).onDeath(player);
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
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = blueSpawns.getJSONObject(new Random().nextInt(blueSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);
            player.getKit().onGameStart(player);
            if (shooter != null) {
                KillMessage killMessage = (KillMessage) shooter.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player1, shooter, player, null, KillMessage.KillReason.PAINTBALL, EngineAPI.getActiveGameInfo().getId())));
                }
                EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.DEATH, new JSONObject().put("player", player.getByDisguiseName()).put("killer", shooter.getByDisguiseName()).put("final", false)));
            } else {
                EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.DEATH, new JSONObject().put("player", player.getByDisguiseName()).put("killer", "None").put("final", false)));
                KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player1, null, player, null, KillMessage.KillReason.PAINTBALL, EngineAPI.getActiveGameInfo().getId())));
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(PlayerDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball) {
            Snowball snowball = (Snowball) e.getDamager();
            if (snowball.getShooter() instanceof ArmorStand) {
                if (!EngineAPI.getActiveGame().isDamageEvP()) {
                    e.setCancelled(true);
                    return;
                }
                Turret turret = ((Paintball)EngineAPI.getActiveGame()).getTurrets().get((ArmorStand) snowball.getShooter());
                if (turret.getOwner().getTeam().equals(e.getPlayer().getTeam())) {
                    e.setCancelled(true);
                    return;
                }
                AuroraMCGamePlayer gp = (AuroraMCGamePlayer) turret.getOwner();
                gp.getGameData().put("gold", (int)gp.getGameData().get("gold") + 2);
                int i = (int)gp.getGameData().get("gold");
                if (i == 0) {
                    i = 1;
                }
                gp.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gp.getGameData().get("gold") + " Gold", i).getItemStack());
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
                if (!gp.getInventory().contains(Material.SNOW_BALL, 64)) {
                    if (gp.getInventory().getItem(0) != null && gp.getInventory().getItem(0).getType() == Material.SNOW_BALL) {
                        if (gp.getInventory().getItem(0).getAmount() + amount > 64) {
                            amount = 64 - gp.getInventory().getItem(0).getAmount();
                        }
                        gp.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, gp.getInventory().getItem(0).getAmount() + amount, null).getItemStack());
                    } else {
                        gp.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, amount, null).getItemStack());
                    }
                }
                gp.sendMessage(new TextComponent(TextFormatter.convert("&6+2 Gold")));
                gp.getRewards().addXp("Kills", 25);
                turret.getOwner().getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);
                turret.getOwner().getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills;turret", 1, true);
            }
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
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) ServerAPI.getPlayer((Player) e.getEntity().getShooter());
            gp.getKit().onGameStart(gp);
        }
    }

}

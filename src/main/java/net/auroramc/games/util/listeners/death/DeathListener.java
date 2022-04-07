/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.death;

import com.sun.xml.internal.ws.api.pipe.Engine;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.json.JSONObject;

import java.util.Map;

public class DeathListener implements Listener {

    private final static DeathListener instance;
    private static boolean friendlyFire;

    static {
        instance = new DeathListener();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(pl);
            if (player.isSpectator() || player.isVanished()) {
                e.setCancelled(true);
                return;
            }
            if (EngineAPI.getServerState() != ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                e.setCancelled(true);
                return;
            }
            if (!friendlyFire) {
                if (e instanceof EntityDamageByEntityEvent) {
                    if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                        Player damager = (Player) ((EntityDamageByEntityEvent) e).getDamager();
                        AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(damager);
                        if (player1.getTeam().equals(player.getTeam())) {
                            e.setCancelled(true);
                            return;
                        }
                    } else if (((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
                        if (projectile.getShooter() instanceof Player) {
                            Player damager = (Player) projectile.getShooter();
                            AuroraMCPlayer auroraMCPlayer = AuroraMCAPI.getPlayer(damager);
                            if (auroraMCPlayer.getTeam().equals(player.getTeam())) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
            if (e.getFinalDamage() >= player.getPlayer().getHealth() && !player.isSpectator()) {
                e.setDamage(0);

                Entity entity = null;
                AuroraMCGamePlayer killer = null;
                KillMessage killMessage;
                KillMessage.KillReason killReason = KillMessage.KillReason.MELEE;
                if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                    killMessage = (KillMessage) player.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
                } else {
                    killMessage = (KillMessage) AuroraMCAPI.getCosmetics().get(500);
                }
                if (e instanceof EntityDamageByEntityEvent) {
                    if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                        Player damager = (Player) ((EntityDamageByEntityEvent) e).getDamager();
                        killer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(damager);
                        killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt", Math.round(e.getFinalDamage() * 100), true);
                        switch (e.getCause()) {
                            case PROJECTILE: {
                                killReason = KillMessage.KillReason.BOW;
                                break;
                            }
                            case VOID: {
                                killReason = KillMessage.KillReason.VOID;
                                break;
                            }
                            case FALL: {
                                killReason = KillMessage.KillReason.FALL;
                                break;
                            }
                            case BLOCK_EXPLOSION: {
                                killReason = KillMessage.KillReason.TNT;
                                break;
                            }
                        }
                    } else if (((EntityDamageByEntityEvent) e).getDamager() instanceof TNTPrimed) {
                        TNTPrimed primed = (TNTPrimed) ((EntityDamageByEntityEvent) e).getDamager();
                        if (primed.getSource() instanceof Player) {
                            Player damager = (Player) primed.getSource();
                            killer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(damager);
                            killReason = KillMessage.KillReason.TNT;
                        }
                    } else if (((EntityDamageByEntityEvent) e).getDamager() instanceof Arrow) {
                        Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
                        if (projectile.getShooter() instanceof Player) {
                            Player damager = (Player) projectile.getShooter();
                            killer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(damager);
                            killReason = KillMessage.KillReason.BOW;
                        } else {
                            if (projectile.getShooter() instanceof Entity) {
                                //Damage by entity.
                                entity = (Entity) projectile.getShooter();
                                killReason = KillMessage.KillReason.ENTITY;
                            }
                        }
                        projectile.remove();
                    } else {
                        //Damage by entity.
                        entity = ((EntityDamageByEntityEvent) e).getDamager();
                        killReason = KillMessage.KillReason.ENTITY;
                    }
                } else {
                    switch (e.getCause()) {
                        case FALL: {
                            killReason = KillMessage.KillReason.FALL;
                            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case VOID: {
                            killReason = KillMessage.KillReason.VOID;
                            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case LAVA: {
                            killReason = KillMessage.KillReason.LAVA;
                            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case FIRE_TICK: {
                            killReason = KillMessage.KillReason.FIRE;
                            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case DROWNING: {
                            killReason = KillMessage.KillReason.DROWNING;
                            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        default: {
                            killReason = KillMessage.KillReason.UNKNOWN;
                        }
                    }
                }
                if (killer != null) {
                    if (killer.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                        killMessage = (KillMessage) killer.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
                    } else {
                        killMessage = (KillMessage) AuroraMCAPI.getCosmetics().get(500);
                    }
                    killer.getRewards().addXp("Kills", 25);
                    killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);

                    if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(22))) {
                        killer.getStats().achievementGained(AuroraMCAPI.getAchievement(22), 1, true);
                    }

                    //If there is a killer, give out assists.
                    for (Map.Entry<AuroraMCGamePlayer, Long> entry : player.getLatestHits().entrySet()) {
                        if (System.currentTimeMillis() - entry.getValue() < 60000 && entry.getKey().getId() != killer.getId()) {
                            entry.getKey().getRewards().addXp("Assists", 10);
                            entry.getKey().getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", "You got an assist on player **" + player.getPlayer().getName() + "**!"));
                            entry.getKey().getPlayer().playSound(entry.getKey().getPlayer().getLocation(), Sound.ARROW_HIT, 100, 1);
                        }
                    }
                }

                player.setLastHitAt(-1);
                player.setLastHitBy(null);
                player.getLatestHits().clear();
                player.getPlayer().setFireTicks(0);
                player.setSpectator(true, true);

                EngineAPI.getActiveGame().onDeath(player, killer);

                String finalMessage = killMessage.onKill(killer, player, entity, killReason);

                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));

                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);

                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(player.getPlayer());
                    player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", finalMessage));
                }
            } else if (e instanceof EntityDamageByEntityEvent) {
                if (e.getFinalDamage() > 0 && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                    AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) ((EntityDamageByEntityEvent) e).getDamager());
                    long time = System.currentTimeMillis();
                    player.setLastHitBy(player1);
                    player.setLastHitAt(time);
                    player.getLatestHits().put(player1, time);
                    player1.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt", Math.round(e.getFinalDamage() * 100), true);
                }
            }
        }
    }


    public static void register(boolean friendlyFire) {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
        DeathListener.friendlyFire = friendlyFire;
    }

    public static void unregister() {
        EntityDamageEvent.getHandlerList().unregister(instance);
    }
}

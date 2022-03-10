/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import java.util.Map;

public class DeathRespawnListener implements Listener {

    private final static DeathRespawnListener instance;
    private static long timeout;
    private static boolean friendlyFire;

    static {
        instance = new DeathRespawnListener();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(pl);
            if (!friendlyFire) {
                if (e instanceof EntityDamageByEntityEvent) {
                    if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                        Player damager = (Player) ((EntityDamageByEntityEvent) e).getDamager();
                        AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(damager);
                        if (player1.getTeam().equals(player.getTeam())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            if (e.getFinalDamage() >= player.getPlayer().getHealth() && !player.isSpectator()) {
                e.setCancelled(true);
                boolean finalKill = EngineAPI.getActiveGame().onDeath(player);
                player.setSpectator(true, finalKill);
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
                String message;
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
                    } else {
                        //Damage by entity.
                        entity = ((EntityDamageByEntityEvent) e).getDamager();
                        killReason = KillMessage.KillReason.ENTITY;
                    }
                } else {
                    switch (e.getCause()) {
                        case FALL: {
                            killReason = KillMessage.KillReason.FALL;
                            if (player.getLastHitBy() != null) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case VOID: {
                            killReason = KillMessage.KillReason.VOID;
                            if (player.getLastHitBy() != null) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case LAVA: {
                            killReason = KillMessage.KillReason.LAVA;
                            if (player.getLastHitBy() != null) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case FIRE_TICK: {
                            killReason = KillMessage.KillReason.FIRE;
                            if (player.getLastHitBy() != null) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        case DROWNING: {
                            killReason = KillMessage.KillReason.DROWNING;
                            if (player.getLastHitBy() != null) {
                                killer = player.getLastHitBy();
                            }
                            break;
                        }
                        default: {
                            killReason = KillMessage.KillReason.UNKNOWN;
                        }
                    }
                }

                //Bukkit.broadcastMessage(" " + killer + "  " + player + " " + entity + " " + killReason);

                if (killer != null) {
                    if (killer.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                        killMessage = (KillMessage) killer.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
                    }
                    killer.getRewards().addXp("Kills", 25);
                    killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);

                    //If there is a killer, give out assists.
                    for (Map.Entry<AuroraMCGamePlayer, Long> entry : player.getLatestHits().entrySet()) {
                        if (System.currentTimeMillis() - entry.getValue() < 60000 && entry.getKey().getId() != killer.getId()) {
                            entry.getKey().getRewards().addXp("Assists", 10);
                            entry.getKey().getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", "You got an assist on player **" + player.getPlayer().getName() + "**!"));
                            entry.getKey().getPlayer().playSound(entry.getKey().getPlayer().getLocation(), Sound.ARROW_HIT, 100, 1);
                        }
                    }
                }

                String finalMessage = killMessage.onKill(killer, player, entity, killReason) + ((finalKill)?" &3&lFINAL KILL!":"");

                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);

                player.setLastHitAt(-1);
                player.setLastHitBy(null);
                player.getLatestHits().clear();
                player.getPlayer().getInventory().clear();
                player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
                player.getPlayer().setFireTicks(0);

                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(player.getPlayer());
                    player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", finalMessage));
                }

                if (!finalKill) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            //Check if they are still connected.
                            if (player.getPlayer().isOnline()) {
                                player.getPlayer().setFallDistance(0);
                                player.getPlayer().setFireTicks(0);
                                player.getPlayer().setVelocity(new Vector(0, 0, 0));
                                EngineAPI.getActiveGame().onRespawn(player);
                                for (Player player2 : Bukkit.getOnlinePlayers()) {
                                    player2.showPlayer(player.getPlayer());
                                }
                                player.setSpectator(false, false);
                            }
                        }
                    }.runTaskLater(AuroraMCAPI.getCore(), timeout);
                } else {
                    EngineAPI.getActiveGame().onFinalKill(player);
                }

            } else if (e instanceof EntityDamageByEntityEvent) {
                if (e.getFinalDamage() > 0 && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                    AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) ((EntityDamageByEntityEvent) e).getDamager());
                    long time = System.currentTimeMillis();
                    player.setLastHitBy(player1);
                    player.setLastHitAt(time);
                    player.getLatestHits().put(player1, time);
                }
            }
        }
    }


    public static void register(int timeout, boolean friendlyFire) {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
        DeathRespawnListener.timeout = timeout;
        DeathRespawnListener.friendlyFire = friendlyFire;
    }

    public static void unregister() {
        EntityDamageEvent.getHandlerList().unregister(instance);
    }
}

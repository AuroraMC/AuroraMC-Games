/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.util.listeners.death;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.*;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.text.WordUtils;
import org.bukkit.*;
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
    public void onDamage(PlayerDamageEvent e) {
        AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
        if (player.isSpectator() || player.isVanished()) {
            e.setCancelled(true);
            if (e.getCause() == PlayerDamageEvent.DamageCause.VOID) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
            return;
        } else {
            if (e instanceof  PlayerDamageByPlayerEvent) {
                AuroraMCGamePlayer damager = (AuroraMCGamePlayer) ((PlayerDamageByPlayerEvent) e).getDamager();
                if (damager.isSpectator() || damager.isVanished()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }
        if (!friendlyFire) {
            if (e instanceof PlayerDamageByPlayerEvent) {
                if (!(e instanceof PlayerDamageByPlayerRangedEvent)) {
                    AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) ((PlayerDamageByPlayerEvent) e).getDamager();
                    if (player1.getTeam().equals(player.getTeam())) {
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    AuroraMCServerPlayer auroraMCPlayer = ((PlayerDamageByPlayerRangedEvent) e).getDamager();
                    if (auroraMCPlayer.getTeam().equals(player.getTeam())) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
        if (!EngineAPI.getActiveGame().isDamageAll()) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamage() >= player.getHealth() && !player.isSpectator()) {
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
            if (e instanceof PlayerDamageByPlayerEvent) {
                if (!EngineAPI.getActiveGame().isDamagePvP()) {
                    e.setCancelled(true);
                    return;
                }
                if (e instanceof PlayerDamageByPlayerRangedEvent) {
                    killer = (AuroraMCGamePlayer) ((PlayerDamageByPlayerRangedEvent) e).getDamager();
                    killReason = KillMessage.KillReason.BOW;
                    ((PlayerDamageByPlayerRangedEvent) e).getProjectile().remove();
                } else {
                    killer = (AuroraMCGamePlayer) ((PlayerDamageByPlayerEvent) e).getDamager();
                    if (killer.isSpectator()) {
                        e.setCancelled(true);
                        return;
                    }
                    killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt", Math.round(e.getDamage() * 100), true);
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
                            if (!EngineAPI.getActiveGame().isDamageFall()) {
                                e.setCancelled(true);
                                return;
                            }
                            if (EngineAPI.getActiveGameInfo().getId() == 102) {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(146))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(146), 1, true);
                                }
                            }
                            killReason = KillMessage.KillReason.FALL;
                            break;
                        }
                        case BLOCK_EXPLOSION: {
                            killReason = KillMessage.KillReason.TNT;
                            break;
                        }
                    }
                }
            } else if (e instanceof PlayerDamageByEntityEvent) {
                if (((PlayerDamageByEntityEvent) e).getDamager() instanceof TNTPrimed) {
                    TNTPrimed primed = (TNTPrimed) ((PlayerDamageByEntityEvent) e).getDamager();
                    if (primed.getSource() instanceof Player) {
                        Player damager = (Player) primed.getSource();
                        killer = (AuroraMCGamePlayer) ServerAPI.getPlayer(damager);
                        killReason = KillMessage.KillReason.TNT;
                    }
                } else if (((PlayerDamageByEntityEvent) e).getDamager() instanceof Arrow) {
                    if (((Arrow) ((PlayerDamageByEntityEvent) e).getDamager()).getShooter() instanceof Entity) {
                        //Damage by entity.
                        if (!EngineAPI.getActiveGame().isDamageEvP()) {
                            e.setCancelled(true);
                            return;
                        }
                        entity = (Entity) ((Arrow) ((PlayerDamageByEntityEvent) e).getDamager()).getShooter();
                        killReason = KillMessage.KillReason.ENTITY;
                    }
                } else {
                    if (!EngineAPI.getActiveGame().isDamageEvP()) {
                        e.setCancelled(true);
                        return;
                    }
                    entity = ((PlayerDamageByEntityEvent) e).getDamager();
                    killReason = KillMessage.KillReason.ENTITY;
                }
            } else {
                switch (e.getCause()) {
                    case FALL: {
                        if (!EngineAPI.getActiveGame().isDamageFall()) {
                            e.setCancelled(true);
                            return;
                        }
                        killReason = KillMessage.KillReason.FALL;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        if (EngineAPI.getActiveGameInfo().getId() == 102) {
                            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(146))) {
                                player.getStats().achievementGained(AuroraMCAPI.getAchievement(146), 1, true);
                            }
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
                killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills;" + killReason.name(), 1, true);
                if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(22))) {
                    killer.getStats().achievementGained(AuroraMCAPI.getAchievement(22), 1, true);
                }

                //If there is a killer, give out assists.
                for (Map.Entry<AuroraMCGamePlayer, Long> entry : player.getLatestHits().entrySet()) {
                    if (System.currentTimeMillis() - entry.getValue() < 60000 && entry.getKey().getId() != killer.getId()) {
                        entry.getKey().getRewards().addXp("Assists", 10);
                        entry.getKey().sendMessage(TextFormatter.pluginMessage("Kill", "You got an assist on player **" + player.getByDisguiseName() + "**!"));
                        entry.getKey().playSound(entry.getKey().getLocation(), Sound.ARROW_HIT, 100, 1);
                    }
                }
            }

            boolean finalKill = EngineAPI.getActiveGame().onDeath(player, killer);

            if (timeout > 0) {
                player.setSpectator(true, finalKill);
            }

            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT)) {
                ((DeathEffect) player.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT)).onDeath(player);
            }

            if (timeout > 0) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
            }

            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths;" + killReason.name(), 1, true);

            player.setLastHitAt(-1);
            player.setLastHitBy(null);
            player.getLatestHits().clear();
            if (!EngineAPI.getActiveGame().isKeepInventory()) {
                player.getInventory().clear();
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getInventory().setBoots(new ItemStack(Material.AIR));
            }
            player.setFireTicks(0);

            String ent = ((entity == null)?null: WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));

            for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                if (timeout > 0) {
                    player2.hidePlayer(player);
                }
                player2.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player2, killer, player, ent, killReason, EngineAPI.getActiveGameInfo().getId()) + ((finalKill) ? " §c§lFINAL KILL!" : "")));
            }

            if (!finalKill) {
                if (timeout > 0) {
                    TextComponent title = new TextComponent("You Died!");
                    title.setColor(ChatColor.RED.asBungee());
                    title.setBold(true);

                    player.sendTitle(title, TextFormatter.highlight("You will respawn in **" + (timeout / 20) + "** seconds!"), 20, 100, 20);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //Check if they are still connected.
                        if (player.isOnline()) {
                            player.setFallDistance(0);
                            player.setFireTicks(0);
                            player.setVelocity(new Vector(0, 0, 0));
                            EngineAPI.getActiveGame().onRespawn(player);
                            player.sendMessage(TextFormatter.pluginMessage("Game", "You respawned!"));
                            if (timeout > 0) {
                                for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                                    player2.showPlayer(player);
                                }
                                player.setSpectator(false, false);
                                TextComponent title = new TextComponent("You Respawned!");
                                title.setColor(ChatColor.DARK_AQUA.asBungee());
                                title.setBold(true);
                                player.sendTitle(title, new TextComponent(""), 20, 100, 20);
                            }
                        }
                    }
                }.runTaskLater(ServerAPI.getCore(), timeout);
            } else {
                EngineAPI.getActiveGame().onFinalKill(player);
            }

        } else if (e instanceof PlayerDamageByPlayerEvent) {
            if (!EngineAPI.getActiveGame().isDamagePvP()) {
                e.setCancelled(true);
                return;
            }
            if (e.getDamage() > 0) {
                AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) ((PlayerDamageByPlayerEvent) e).getDamager();
                if (!player1.isSpectator()) {
                    long time = System.currentTimeMillis();
                    player.setLastHitBy(player1);
                    player.setLastHitAt(time);
                    player.getLatestHits().put(player1, time);
                    player1.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt", Math.round(e.getDamage() * 100), true);
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (e instanceof PlayerDamageByEntityEvent) {
            if (!EngineAPI.getActiveGame().isDamageEvP()) {
                e.setCancelled(true);
            }
        } else if (e.getCause() == PlayerDamageEvent.DamageCause.FALL && !EngineAPI.getActiveGame().isDamageFall()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByPlayerEvent e) {
        if (!EngineAPI.getActiveGame().isDamagePvE()) {
            e.setCancelled(true);
        }
    }


    public static void register(int timeout, boolean friendlyFire) {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
        DeathRespawnListener.timeout = timeout;
        DeathRespawnListener.friendlyFire = friendlyFire;
    }

    public static void unregister() {
        PlayerDamageEvent.getHandlerList().unregister(instance);
    }

    public static long getTimeout() {
        return timeout;
    }
}

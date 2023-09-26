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
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.json.JSONObject;

import java.util.Map;

public class NoDamageInstaKillListener implements Listener {

    private final static NoDamageInstaKillListener instance;

    static {
        instance = new NoDamageInstaKillListener();
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
        }
        if (!EngineAPI.getActiveGame().isDamageAll()) {
            e.setCancelled(true);
            return;
        }
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }
        if ((e.getCause() == PlayerDamageEvent.DamageCause.LAVA || e.getCause() == PlayerDamageEvent.DamageCause.VOID) && !player.isSpectator()) {
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
                    if (killer != null && killer.isSpectator()) {
                        e.setCancelled(true);
                        return;
                    }
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

            player.setLastHitAt(-1);
            player.setLastHitBy(null);
            player.getLatestHits().clear();
            player.setFireTicks(0);
            player.setSpectator(true, true);

            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT)) {
                ((DeathEffect) player.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT)).onDeath(player);
            }

            EngineAPI.getActiveGame().onDeath(player, killer);

            JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
            int x, y, z;
            x = specSpawn.getInt("x");
            y = specSpawn.getInt("y");
            z = specSpawn.getInt("z");
            float yaw = specSpawn.getFloat("yaw");
            player.teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));

            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths;" + killReason.name(), 1, true);

            String ent = ((entity == null)?null: WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));

            for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                player2.hidePlayer(player);
                player2.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player2, killer, player, ent, killReason, EngineAPI.getActiveGameInfo().getId())));
            }
        } else if (e instanceof PlayerDamageByPlayerEvent) {
            if (!EngineAPI.getActiveGame().isDamagePvP() || ((AuroraMCGamePlayer)((PlayerDamageByPlayerEvent) e).getDamager()).isSpectator()) {
                e.setCancelled(true);
                return;
            }
            e.setDamage(0);
            AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) e.getPlayer();
            if (!player1.isSpectator()) {
                long time = System.currentTimeMillis();
                player.setLastHitBy(player1);
                player.setLastHitAt(time);
                player.getLatestHits().put(player1, time);
                player1.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt", Math.round(e.getDamage() * 100), true);
            } else {
                e.setCancelled(true);
            }
        } else if (e instanceof PlayerDamageByEntityEvent) {
            if (!EngineAPI.getActiveGame().isDamageEvP()) {
                e.setCancelled(true);
            }
        } else if (e.getCause() == PlayerDamageEvent.DamageCause.FALL && !EngineAPI.getActiveGame().isDamageFall()) {
            e.setCancelled(true);
        } else {
            e.setDamage(0);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByPlayerEvent e) {
        if (!EngineAPI.getActiveGame().isDamagePvE()) {
            e.setCancelled(true);
        }
    }


    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerDamageEvent.getHandlerList().unregister(instance);
    }
}

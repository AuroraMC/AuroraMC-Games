/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.tag.teams.TaggedTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.stream.Collectors;

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
                if (!player.isSpectator() && !player.isVanished() && player.getTeam() instanceof TaggedTeam) {
                    AuroraMCGamePlayer hit = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
                    if (hit.getTeam() instanceof TaggedTeam) {
                        e.setCancelled(true);
                    } else {
                        e.setDamage(0);
                        hit.setTeam(player.getTeam());
                        hit.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You were tagged!"));
                        for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                            player1.updateNametag(hit);
                            if (player1.equals(hit)) {
                                if (player1.isDisguised()) {
                                    if (player1.getPreferences().isHideDisguiseNameEnabled()) {
                                        player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + hit.getName() + "** was tagged by **" + player.getPlayer().getName() + "**!"));
                                        continue;
                                    }
                                }
                                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + hit.getPlayer().getName() + "** was tagged by **" + player.getPlayer().getName() + "**!"));
                            } else if (player1.equals(player)) {
                                if (player1.isDisguised()) {
                                    if (player1.getPreferences().isHideDisguiseNameEnabled()) {
                                        player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + hit.getPlayer().getName() + "** was tagged by **" + player.getName() + "**!"));
                                        continue;
                                    }
                                }
                                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + hit.getPlayer().getName() + "** was tagged by **" + player.getPlayer().getName() + "**!"));
                            } else {
                                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + hit.getPlayer().getName() + "** was tagged by **" + player.getPlayer().getName() + "**!"));
                            }
                        }
                        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(pl2 -> !((AuroraMCGamePlayer)pl2).isSpectator() && !(pl2.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
                        if (playersAlive.size() == 1) {
                            EngineAPI.getActiveGame().end(playersAlive.get(0));
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
    }

}
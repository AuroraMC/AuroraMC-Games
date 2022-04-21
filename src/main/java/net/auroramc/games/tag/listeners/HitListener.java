/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.tag.teams.RunnersTeam;
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
                AuroraMCGamePlayer hit = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
                if (!player.isSpectator() && !player.isVanished() && player.getTeam() instanceof TaggedTeam && hit.getTeam() instanceof RunnersTeam) {
                    e.setDamage(0);
                    hit.setTeam(player.getTeam());
                    hit.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You were tagged!"));
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tags", 1, true);
                    hit.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tagged", 1, true);

                    KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));

                    for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                        player1.updateNametag(hit);
                        player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, player, hit, null, KillMessage.KillReason.TAG)));
                    }
                    List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(pl2 -> !((AuroraMCGamePlayer) pl2).isSpectator() && !(pl2.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
                    if (playersAlive.size() == 1) {
                        EngineAPI.getActiveGame().end(playersAlive.get(0));
                    }
                } else if (!player.isSpectator() && !player.isVanished() && player.getTeam().equals(hit.getTeam())) {
                    e.setDamage(0);
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

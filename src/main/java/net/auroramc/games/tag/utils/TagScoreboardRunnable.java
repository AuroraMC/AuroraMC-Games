/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.utils;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.tag.Tag;
import net.auroramc.games.tag.teams.TaggedTeam;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class TagScoreboardRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (EngineAPI.getActiveGame() != null && EngineAPI.getActiveGame() instanceof Tag) {
            List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !(player.getTeam() instanceof TaggedTeam) && !player.isVanished() && !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
            List<AuroraMCPlayer> taggers = AuroraMCAPI.getPlayers().stream().filter(player -> (player.getTeam() instanceof TaggedTeam) && !player.isVanished() && !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

            double minutes = gametime / 1000d / 60d;
            double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalValue = 0;
            }
            for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                PlayerScoreboard scoreboard = player.getScoreboard();
                scoreboard.setLine(8, "&b&l«TAGGERS»");
                scoreboard.setLine(7, taggers.size() + "");
                scoreboard.setLine(6, "   ");
                scoreboard.setLine(5, "&b&l«RUNNERS»");
                scoreboard.setLine(4, playersAlive.size() + " Remaining");
                scoreboard.setLine(3, "   ");
                scoreboard.setLine(2, "&b&l«GAME TIME»");
                scoreboard.setLine(1, finalValue + " minutes");
            }
        } else {
            this.cancel();
        }
    }
}

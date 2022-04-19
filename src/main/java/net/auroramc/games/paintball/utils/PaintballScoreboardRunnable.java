/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.utils;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import net.auroramc.games.spleef.Spleef;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class PaintballScoreboardRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (EngineAPI.getActiveGame() != null && EngineAPI.getActiveGame() instanceof Paintball) {
            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

            double minutes = gametime / 1000d / 60d;
            double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalValue = 0;
            }
            for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                PlayerScoreboard scoreboard = player.getScoreboard();
                scoreboard.setLine(8, "&c&l«RED LIVES»");
                scoreboard.setLine(7, ((PBRed)EngineAPI.getActiveGame().getTeams().get("Red")).getLives() + " Alive");
                scoreboard.setLine(6, "   ");
                scoreboard.setLine(5, "&9&l«BLUE LIVES»");
                scoreboard.setLine(4, ((PBBlue)EngineAPI.getActiveGame().getTeams().get("Blue")).getLives() + " Alive");
                scoreboard.setLine(3, "   ");
                scoreboard.setLine(2, "&b&l«GAME TIME»");
                scoreboard.setLine(1, finalValue + " minutes");

            }
        } else {
            this.cancel();
        }
    }
}

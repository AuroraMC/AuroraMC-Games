/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.utils;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import org.bukkit.scheduler.BukkitRunnable;

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
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                PlayerScoreboard scoreboard = player.getScoreboard();
                scoreboard.setLine(8, "&c&l«RED LIVES»");
                scoreboard.setLine(7, ((PBRed)EngineAPI.getActiveGame().getTeams().get("Red")).getLives() + "");
                scoreboard.setLine(6, "   ");
                scoreboard.setLine(5, "&9&l«BLUE LIVES»");
                scoreboard.setLine(4, ((PBBlue)EngineAPI.getActiveGame().getTeams().get("Blue")).getLives() + "");
                scoreboard.setLine(3, "   ");
                scoreboard.setLine(2, "&b&l«GAME TIME»");
                scoreboard.setLine(1, finalValue + " minutes");

            }
        } else {
            this.cancel();
        }
    }
}

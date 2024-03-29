/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.utils;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.spleef.Spleef;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class SpleefScoreboardRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (EngineAPI.getActiveGame() != null && EngineAPI.getActiveGame() instanceof Spleef) {
            List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

            double minutes = gametime / 1000d / 60d;
            double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalValue = 0;
            }
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                PlayerScoreboard scoreboard = player.getScoreboard();
                if (playersAlive.size() < 10) {
                    scoreboard.setLine(3, "   ");
                    scoreboard.setLine(2, "&b&l«GAME TIME»");
                    scoreboard.setLine(1,  finalValue + " minutes");
                    int i = 4;
                    for (AuroraMCServerPlayer player1 : playersAlive) {
                        if (player1.equals(player)) {
                            if (player1.isDisguised() && player1.getPreferences().isHideDisguiseNameEnabled()) {
                                scoreboard.setLine(i, "&e" + player1.getName());
                                continue;
                            }
                        }
                        if (player1.isDisguised()) {
                            scoreboard.setLine(i, "&e" + player1.getActiveDisguise().getName());
                        } else {
                            scoreboard.setLine(i, "&e" + player1.getName());
                        }
                        i++;
                    }
                    scoreboard.setLine(i, "&b&l«PLAYERS»");
                    i++;
                    while (i < 15) {
                        scoreboard.clearLine(i);
                        i++;
                    }

                } else {
                    scoreboard.setLine(5, "&b&l«PLAYERS»");
                    scoreboard.setLine(4, playersAlive.size() + " Alive");
                    scoreboard.setLine(3, "   ");
                    scoreboard.setLine(2, "&b&l«GAME TIME»");
                    scoreboard.setLine(1,  finalValue + " minutes");
                }
            }
        } else {
            this.cancel();
        }
    }
}

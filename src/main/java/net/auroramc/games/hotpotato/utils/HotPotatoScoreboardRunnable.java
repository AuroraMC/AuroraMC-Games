/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.utils;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.spleef.Spleef;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class HotPotatoScoreboardRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (EngineAPI.getActiveGame() != null && EngineAPI.getActiveGame() instanceof Spleef) {
            List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

            double minutes = gametime / 1000d / 60d;
            double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalValue = 0;
            }
            for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                PlayerScoreboard scoreboard = player.getScoreboard();
                if (playersAlive.size() < 7) {
                    scoreboard.setLine(6, " ");
                    scoreboard.setLine(5, "&b&l«POTATOES»");
                    scoreboard.setLine(4,  ((HotPotato)EngineAPI.getActiveGame()).getPotatoes() + "");
                    scoreboard.setLine(3, "   ");
                    scoreboard.setLine(2, "&b&l«GAME TIME»");
                    scoreboard.setLine(1,  finalValue + " minutes");
                    int i = 4;
                    for (AuroraMCPlayer player1 : playersAlive) {
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
                    scoreboard.setLine(8, "&b&l«PLAYERS»");
                    scoreboard.setLine(7, playersAlive.size() + " Alive");
                    scoreboard.setLine(6, " ");
                    scoreboard.setLine(5, "&b&l«POTATOES»");
                    scoreboard.setLine(4,  ((HotPotato)EngineAPI.getActiveGame()).getPotatoes() + "");
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

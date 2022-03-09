/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerScoreboard;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardRunnable extends BukkitRunnable {

    private final CQBlue blue;
    private final CQRed red;

    public ScoreboardRunnable(CQBlue blue, CQRed red)  {
        this.blue = blue;
        this.red = red;
    }

    @Override
    public void run() {
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            PlayerScoreboard scoreboard = player.getScoreboard();
            scoreboard.setTitle("&3&l-= &b&lCRYSTAL QUEST &3&l=-");


            if (blue.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                scoreboard.setLine(13, "&9&l«BLUE ALIVE»");
                scoreboard.setLine(12, "" + AuroraMCAPI.getPlayers().stream().filter(auroraMCPlayer -> !player.isDead() && player.getTeam() instanceof CQBlue).count());
            } else {
                scoreboard.setLine(13, "&9&l«BLUE CRYSTALS»");
                scoreboard.setLine(12, "&" + ((blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰");
            }
            scoreboard.setLine(11, " ");
            if (red.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                scoreboard.setLine(10, "&c&l«RED ALIVE»");
                scoreboard.setLine(9, "" + AuroraMCAPI.getPlayers().stream().filter(auroraMCPlayer -> !player.isDead() && player.getTeam() instanceof CQRed).count());
            } else {
                scoreboard.setLine(10, "&c&l«RED CRYSTALS»");
                scoreboard.setLine(9, "&" + ((red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰");
            }

            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

            double minutes = gametime / 1000d / 60d;
            double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalValue = 0;
            }

            double timeTillReset = ((gametime < 180000L)?(180000 - gametime):(300000 - ((gametime - 180000) % 300000)));
            double finalTimeTillReset = (double)Math.round(timeTillReset * 10.0D) / 10.0D;
            if (gametime < 0) {
                finalTimeTillReset = 3;
            }

            scoreboard.setLine(8, "  ");
            scoreboard.setLine(7, "&b&l«MINE RESET»");
            scoreboard.setLine(6,  finalTimeTillReset + " minutes");
            scoreboard.setLine(5, "    ");
            scoreboard.setLine(4, "&b&l«GAME TIME»");
            scoreboard.setLine(3,  finalValue + " minutes");
            scoreboard.setLine(2, "    ");

        }
    }
}

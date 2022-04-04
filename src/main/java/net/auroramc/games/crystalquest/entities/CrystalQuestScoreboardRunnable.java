/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.scheduler.BukkitRunnable;

public class CrystalQuestScoreboardRunnable extends BukkitRunnable {

    private final CQBlue blue;
    private final CQRed red;

    public CrystalQuestScoreboardRunnable(CQBlue blue, CQRed red)  {
        this.blue = blue;
        this.red = red;
    }

    @Override
    public void run() {
        long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;

        double minutes = gametime / 1000d / 60d;
        double finalValue = (double)Math.round(minutes * 10.0D) / 10.0D;
        if (gametime < 0) {
            finalValue = 0;
        }

        double timeTillReset = ((gametime < 180000L)?(180000 - gametime):(300000 - ((gametime - 180000) % 300000))) / 1000d / 60d;
        double finalTimeTillReset = (double)Math.round(timeTillReset * 10.0D) / 10.0D;
        if (gametime < 0) {
            finalTimeTillReset = 3;
        }

        long blueAlive = EngineAPI.getActiveGame().getTeams().get("Blue").getPlayers().stream().filter(auroraMCPlayer -> (!auroraMCPlayer.isDead())).count();
        long redAlive = EngineAPI.getActiveGame().getTeams().get("Red").getPlayers().stream().filter(auroraMCPlayer -> (!auroraMCPlayer.isDead())).count();


        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            PlayerScoreboard scoreboard = player.getScoreboard();
            scoreboard.setTitle("&3&l-= &b&lCRYSTAL QUEST &3&l=-");


            if (blue.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                scoreboard.setLine(14, "&9&l«BLUE ALIVE»");
                scoreboard.setLine(13, "" + blueAlive);
            } else {
                scoreboard.setLine(14, "&9&l«BLUE CRYSTALS»");
                scoreboard.setLine(13, "&" + ((blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰");
            }
            scoreboard.setLine(12, " ");
            if (red.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                scoreboard.setLine(11, "&c&l«RED ALIVE»");
                scoreboard.setLine(10, "" + redAlive + " ");
            } else {
                scoreboard.setLine(11, "&c&l«RED CRYSTALS»");
                scoreboard.setLine(10, "&" + ((red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ &" + ((red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME)?'a':(red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED)?'e':'c') + "✰ ");
            }
            scoreboard.setLine(9, "    ");
            scoreboard.setLine(8, "&b&l«TEAM LIVES»");
            scoreboard.setLine(7,  ((player.getTeam() != null)?((player.getTeam() instanceof CQBlue)?((CQBlue) player.getTeam()).getLives():((CQRed)player.getTeam()).getLives()):"N/A")  + "");
            scoreboard.setLine(6, "  ");
            scoreboard.setLine(5, "&b&l«MINE RESET»");
            scoreboard.setLine(4,  finalTimeTillReset + " minutes ");
            scoreboard.setLine(3, "   ");
            scoreboard.setLine(2, "&b&l«GAME TIME»");
            scoreboard.setLine(1,  finalValue + " minutes");

        }
    }
}

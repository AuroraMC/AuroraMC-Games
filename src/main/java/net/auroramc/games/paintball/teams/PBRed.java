/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.teams;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;

import java.util.ArrayList;
import java.util.List;

public class PBRed implements Team {

    private final List<AuroraMCPlayer> players;
    private int lives;

    public PBRed() {
        players = new ArrayList<>();
    }

    public void initLives() {
        lives = ((players.size() <= 5)?50:players.size()*10);
    }


    public int getLives() {
        return lives;
    }

    public synchronized int addLife() {
        return lives++;
    }

    public synchronized int removeLife() {
        return lives--;
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public char getTeamColor() {
        return 'c';
    }

    @Override
    public String getName() {
        return "Red";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }
}

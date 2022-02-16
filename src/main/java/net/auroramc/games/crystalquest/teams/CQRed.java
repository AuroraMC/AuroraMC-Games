/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.teams;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;

import java.util.ArrayList;
import java.util.List;

public class CQRed implements Team {

    private final List<AuroraMCPlayer> players;
    private int crystalsCaptured;

    public CQRed() {
        players = new ArrayList<>();
        crystalsCaptured = 0;
    }


    @Override
    public int getId() {
        return 0;
    }

    public int getCrystalsCaptured() {
        return crystalsCaptured;
    }

    public void setCrystalsCaptured(int crystalsCaptured) {
        this.crystalsCaptured = crystalsCaptured;
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

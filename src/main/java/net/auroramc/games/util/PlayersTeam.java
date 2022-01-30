/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;

import java.util.ArrayList;
import java.util.List;

public class PlayersTeam implements Team {

    private final List<AuroraMCPlayer> players;

    public PlayersTeam() {
        players = new ArrayList<>();
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public char getTeamColor() {
        return 'e';
    }

    @Override
    public String getName() {
        return "Players";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }
}

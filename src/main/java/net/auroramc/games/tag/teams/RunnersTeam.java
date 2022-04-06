/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.teams;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;

import java.util.ArrayList;
import java.util.List;

public class RunnersTeam implements Team {

    private final List<AuroraMCPlayer> players;

    public RunnersTeam() {
        players = new ArrayList<>();
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public char getTeamColor() {
        return 'a';
    }

    @Override
    public String getName() {
        return "Runners";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }
}

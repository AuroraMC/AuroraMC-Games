/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.teams;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;

import java.util.ArrayList;
import java.util.List;

public class TaggedTeam implements Team {

    private final List<AuroraMCPlayer> players;

    public TaggedTeam() {
        players = new ArrayList<>();
    }


    @Override
    public int getId() {
        return 1;
    }

    @Override
    public char getTeamColor() {
        return 'c';
    }

    @Override
    public String getName() {
        return "Tagged";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }
}

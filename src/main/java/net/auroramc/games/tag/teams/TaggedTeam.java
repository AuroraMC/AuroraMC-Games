/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag.teams;


import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.md_5.bungee.api.ChatColor;

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
    public ChatColor getTeamColor() {
        return ChatColor.RED;
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

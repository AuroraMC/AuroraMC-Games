/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util;


import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.md_5.bungee.api.ChatColor;

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
    public ChatColor getTeamColor() {
        return ChatColor.YELLOW;
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

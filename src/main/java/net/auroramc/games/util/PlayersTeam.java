/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

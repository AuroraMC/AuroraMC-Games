/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.teams;

import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.paintball.Paintball;
import net.md_5.bungee.api.ChatColor;

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

    public synchronized void addLife() {
        lives += ((Paintball) EngineAPI.getActiveGame()).getLivesPerKill();
    }

    public synchronized void removeLife() {
        lives -= ((Paintball) EngineAPI.getActiveGame()).getLivesPerKill();
    }

    public synchronized void plus3Lives() {
        lives += 3;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ChatColor getTeamColor() {
        return ChatColor.RED;
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

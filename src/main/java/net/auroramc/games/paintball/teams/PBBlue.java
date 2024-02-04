/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.teams;

import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.paintball.Paintball;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PBBlue implements Team {

    private final List<AuroraMCPlayer> players;
    private int lives;

    public PBBlue() {
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
        return 1;
    }

    @Override
    public ChatColor getTeamColor() {
        return ChatColor.BLUE;
    }

    @Override
    public String getName() {
        return "Blue";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }
}

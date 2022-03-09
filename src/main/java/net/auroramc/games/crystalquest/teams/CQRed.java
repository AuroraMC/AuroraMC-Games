/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.teams;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.games.crystalquest.entities.Crystal;

import java.util.ArrayList;
import java.util.List;

public class CQRed implements Team {

    private final List<AuroraMCPlayer> players;
    private int crystalsCaptured;
    private Crystal bossCrystal;
    private Crystal towerACrystal;
    private Crystal towerBCrystal;
    private int lives;

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

    public Crystal getBossCrystal() {
        return bossCrystal;
    }

    public Crystal getTowerACrystal() {
        return towerACrystal;
    }

    public Crystal getTowerBCrystal() {
        return towerBCrystal;
    }

    public void setBossCrystal(Crystal bossCrystal) {
        this.bossCrystal = bossCrystal;
    }

    public void setTowerACrystal(Crystal towerACrystal) {
        this.towerACrystal = towerACrystal;
    }

    public void setTowerBCrystal(Crystal towerBCrystal) {
        this.towerBCrystal = towerBCrystal;
    }

    public int getLives() {
        return lives;
    }

    public void lostLife() {
        lives--;
    }

    public void lifeBrought() {
        lives++;
    }
}
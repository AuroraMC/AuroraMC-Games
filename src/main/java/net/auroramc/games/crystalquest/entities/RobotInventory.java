/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.engine.api.players.AuroraMCGamePlayer;

public class RobotInventory {

    private final AuroraMCGamePlayer player;
    private final MiningRobot robot;
    private int iron;
    private int gold;

    public RobotInventory(AuroraMCGamePlayer player, MiningRobot robot) {
        this.player = player;
        this.robot = robot;
    }

    public void addIron(int amount) {
        iron += amount;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public int getGold() {
        return gold;
    }

    public int getIron() {
        return iron;
    }

    public int withdrawIron() {
        int oldIron = iron;
        this.iron = 0;
        return oldIron;
    }

    public int withdrawGold() {
        int oldGold = gold;
        this.gold = 0;
        return oldGold;
    }
}

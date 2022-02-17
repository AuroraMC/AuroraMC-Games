/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;

public class Crystal {

    private EnderCrystal crystal;
    private final Location home;
    private CrystalState state;
    private AuroraMCGamePlayer holder;
    private boolean boss;

    public Crystal(Location location, boolean boss) {
        this.home = location;
        this.state = CrystalState.AT_HOME;
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
        this.boss = boss;
    }

    public void crystalCaptured(AuroraMCGamePlayer holder) {
        this.holder = holder;
        crystal.remove();
        crystal = null;
        state = CrystalState.CAPTURED;
    }

    public void crystalDead() {
        this.state = CrystalState.DEAD;
        this.holder = null;
    }

    public void crystalReturned() {
        this.holder = null;
        crystal = EngineAPI.getMapWorld().spawn(home, EnderCrystal.class);
    }

    public EnderCrystal getCrystal() {
        return crystal;
    }

    public AuroraMCGamePlayer getHolder() {
        return holder;
    }


    public Location getHome() {
        return home;
    }

    public CrystalState getState() {
        return state;
    }

    public boolean isBoss() {
        return boss;
    }

    public enum CrystalState {AT_HOME, CAPTURED, DEAD}

}

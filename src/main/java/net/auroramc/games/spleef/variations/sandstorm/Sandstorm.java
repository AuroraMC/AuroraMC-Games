/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.variations.sandstorm;

import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.run.Run;
import net.auroramc.games.run.variations.RunVariation;
import net.auroramc.games.spleef.Spleef;
import net.auroramc.games.spleef.variations.SpleefVariation;
import net.auroramc.games.spleef.variations.sandstorm.kits.SandstormSnowman;
import org.bukkit.Material;

public class Sandstorm extends SpleefVariation {


    public Sandstorm(Spleef game) {
        super(game);
    }


    @Override
    public boolean preLoad() {
        getGame().setBlockPlace(true);
        getGame().getKits().clear();
        getGame().getKits().add(new SandstormSnowman());
        return false;
    }

    @Override
    public boolean load(GameMap gameMap) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public void inProgress() {
        getGame().getBreakListener().getMaterial().add(Material.SAND);
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public boolean onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {
        return false;
    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void onRespawn(AuroraMCGamePlayer player) {
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer player, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return true;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }


    @Override
    public void balanceTeams() {
        GameStartingRunnable.teamBalance();
    }
}

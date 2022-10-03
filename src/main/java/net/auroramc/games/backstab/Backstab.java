/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.backstab;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.entity.Player;

public class Backstab extends Game {


    public Backstab(GameVariation gameVariation) {
        super(gameVariation);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load(GameMap gameMap) {

    }

    @Override
    public void generateTeam(AuroraMCPlayer auroraMCPlayer) {

    }

    @Override
    public void onPlayerJoin(Player player) {

    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void onRespawn(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }
}

/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.backstab;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.entity.Player;

public class Backstab extends Game {


    public Backstab(GameVariationInfo gameVariation) {
        super(gameVariation);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load(GameMap gameMap) {

    }

    @Override
    public void generateTeam(AuroraMCServerPlayer auroraMCPlayer) {

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

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CrystalQuest extends Game {


    public CrystalQuest(GameVariation gameVariation) {
        super(gameVariation);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load(GameMap gameMap) {

    }

    @Override
    public void start() {

    }

    @Override
    public void onPlayerJoin(Player player) {

    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public List<Kit> getKits() {
        return new ArrayList<>();
    }
}

/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.crystalquest.CrystalQuestInfo;
import net.auroramc.games.ffa.FFAInfo;
import net.auroramc.games.spleef.SpleefInfo;
import org.bukkit.plugin.java.JavaPlugin;

public class AuroraMCGames extends JavaPlugin {

    @Override
    public void onEnable() {
        EngineAPI.registerGame(new CrystalQuestInfo());
        EngineAPI.registerGame(new SpleefInfo());
        EngineAPI.registerGame(new FFAInfo());
        EngineAPI.loadRotation();
    }

}

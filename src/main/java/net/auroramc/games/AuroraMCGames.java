/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.games.crystalquest.CrystalQuestInfo;
import net.auroramc.games.ffa.FFAInfo;
import net.auroramc.games.hotpotato.HotPotatoInfo;
import net.auroramc.games.paintball.PaintballInfo;
import net.auroramc.games.run.RunInfo;
import net.auroramc.games.spleef.SpleefInfo;
import net.auroramc.games.tag.TagInfo;
import net.auroramc.games.util.listeners.death.BorderListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AuroraMCGames extends JavaPlugin {

    @Override
    public void onEnable() {
        EngineAPI.registerGame(new CrystalQuestInfo());
        EngineAPI.registerGame(new SpleefInfo());
        EngineAPI.registerGame(new FFAInfo());
        EngineAPI.registerGame(new HotPotatoInfo());
        EngineAPI.registerGame(new RunInfo());
        EngineAPI.registerGame(new TagInfo());
        EngineAPI.registerGame(new PaintballInfo());
        EngineAPI.loadRotation();

        Bukkit.getPluginManager().registerEvents(new BorderListener(), this);
    }

}

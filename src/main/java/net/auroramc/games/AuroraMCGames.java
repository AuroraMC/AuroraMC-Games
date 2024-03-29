/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.core.api.ServerAPI;
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
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

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
        ServerInfo info = ((ServerInfo)AuroraMCAPI.getInfo());
        if (info.getServerType().has("event") && info.getServerType().getBoolean("event")) {
            try {
                ServerAPI.loadEvent();
            } catch (Exception e) {
                AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            }
        }

        EngineAPI.loadRotation();

        Bukkit.getPluginManager().registerEvents(new BorderListener(), this);
    }

}

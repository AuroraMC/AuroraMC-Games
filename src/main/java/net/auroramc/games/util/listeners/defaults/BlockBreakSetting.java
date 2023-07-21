/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockBreakSetting implements Listener {

    private final static BlockBreakSetting instance;

    static {
        instance = new BlockBreakSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockBreak() && !EngineAPI.getActiveGame().isBlockBreakCreative());
        } else {
            e.setCancelled(!EngineAPI.getActiveGame().isBlockBreak());
        }
    }


    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        BlockBreakEvent.getHandlerList().unregister(instance);
    }
}

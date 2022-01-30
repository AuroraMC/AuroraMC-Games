/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.engine.api.EngineAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        }
    }

}

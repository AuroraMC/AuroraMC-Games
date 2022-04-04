/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BreakListener implements Listener {

    private final Material material;

    public BreakListener(Material material) {
        this.material = material;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            if (e.getBlock().getType() != material) {
                e.setCancelled(true);
                e.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MiningListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        switch (e.getBlock().getType()) {
            case EMERALD_ORE: {
                e.setCancelled(true);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.EMERALD));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case IRON_ORE: {
                e.setCancelled(true);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case GOLD_ORE: {
                e.setCancelled(true);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case STONE: {
                e.setCancelled(true);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COBBLESTONE));
                e.getBlock().setType(Material.BEDROCK);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getBlock().setType(Material.STONE);
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 100);
                break;
            }
            case STAINED_CLAY:
            case WOOL:
            case WOOD:
            case STAINED_GLASS: {
                break;
            }
            default: {
                e.setCancelled(true);
            }
        }
    }

}

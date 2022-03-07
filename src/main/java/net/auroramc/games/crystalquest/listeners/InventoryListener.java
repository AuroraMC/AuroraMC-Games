/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            if ((e.getSlot() < 3 || e.getSlot() == 8) && e.getClickedInventory() instanceof PlayerInventory) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot move this item!"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        ((Player)e.getWhoClicked()).updateInventory();
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 3);
            }
        }
    }

}

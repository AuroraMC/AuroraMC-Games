/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableMovableItems implements Listener {

    private final static DisableMovableItems instance;

    static {
        instance = new DisableMovableItems();
    }

    @EventHandler
    public void onMove(InventoryClickEvent e) {
        e.setCancelled(true);
        e.getWhoClicked().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot move this item!"));
        new BukkitRunnable() {
            @Override
            public void run() {
                ((Player) e.getWhoClicked()).updateInventory();
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 3);
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        InventoryClickEvent.getHandlerList().unregister(instance);
    }
}

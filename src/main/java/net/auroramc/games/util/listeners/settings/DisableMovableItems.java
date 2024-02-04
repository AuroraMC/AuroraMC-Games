/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.inventory.InventoryClickEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableMovableItems implements Listener {

    private final static DisableMovableItems instance;

    static {
        instance = new DisableMovableItems();
    }

    @EventHandler
    public void onMove(InventoryClickEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot move this item!"));
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().updateInventory();
            }
        }.runTaskLater(ServerAPI.getCore(), 3);
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        InventoryClickEvent.getHandlerList().unregister(instance);
    }
}

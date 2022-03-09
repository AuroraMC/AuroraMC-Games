/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            if (e.getSlot() == 8 && e.getClickedInventory() instanceof PlayerInventory) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot move this item!"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        ((Player)e.getWhoClicked()).updateInventory();
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 3);
            } else if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
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

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().name().endsWith("_SWORD") || e.getItemDrop().getItemStack().getType().name().endsWith("_PICKAXE") || e.getItemDrop().getItemStack().getType().name().endsWith("_AXE") || e.getItemDrop().getItemStack().getType() == Material.COMPASS || e.getItemDrop().getItemStack().getType().name().endsWith("_HELMET") || e.getItemDrop().getItemStack().getType().name().endsWith("_CHESTPLATE") || e.getItemDrop().getItemStack().getType().name().endsWith("_LEGGINGS") || e.getItemDrop().getItemStack().getType().name().endsWith("_BOOTS")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot drop this item!"));
        }
    }

}
/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            AuroraMCGamePlayer gamePlayer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getWhoClicked());
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
            } else if (gamePlayer.getGameData().containsKey("crystal_possession")) {
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
        if (e.getItemDrop().getItemStack().getType().name().endsWith("_SWORD") || e.getItemDrop().getItemStack().getType().name().endsWith("_PICKAXE") || e.getItemDrop().getItemStack().getType().name().endsWith("_AXE") || e.getItemDrop().getItemStack().getType() == Material.COMPASS || e.getItemDrop().getItemStack().getType().name().endsWith("_HELMET") || e.getItemDrop().getItemStack().getType().name().endsWith("_CHESTPLATE") || e.getItemDrop().getItemStack().getType().name().endsWith("_LEGGINGS") || e.getItemDrop().getItemStack().getType().name().endsWith("_BOOTS") || e.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot drop this item!"));
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.ARROW) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        e.getInventory().setResult(new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.GOLDEN_APPLE) {
            e.setCancelled(true);
            if (e.getItem().getAmount() == 1) {
                e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            } else {
                e.getPlayer().setItemInHand(new ItemStack(Material.GOLDEN_APPLE, e.getItem().getAmount() - 1));
            }
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        }
    }

    @EventHandler
    public void onEat(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.COOKIE) {
            e.setCancelled(true);
            if (e.getItem().getAmount() == 1) {
                e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            } else {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            }
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
        } else if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (player.getGameData().containsKey("crystal_possession")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use ender pearls while you have a crystal!"));
            }
        }

    }

}

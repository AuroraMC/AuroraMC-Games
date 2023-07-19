/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.inventory.CraftItemEvent;
import net.auroramc.core.api.events.inventory.InventoryClickEvent;
import net.auroramc.core.api.events.inventory.PrepareItemCraftEvent;
import net.auroramc.core.api.events.player.*;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.kits.Archer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class InventoryListener implements Listener {

    private final ItemStack stack = new GUIItem(Material.ARROW, "&eArcher's Arrow").getItemStack();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            AuroraMCGamePlayer gamePlayer = (AuroraMCGamePlayer) e.getPlayer();
            if (e.getSlot() == 8 && e.getClickedInventory() instanceof PlayerInventory) {
                e.setCancelled(true);
                gamePlayer.sendMessage(TextFormatter.pluginMessage("Game", "You cannot move this item!"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gamePlayer.updateInventory();
                    }
                }.runTaskLater(ServerAPI.getCore(), 3);
            } else if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                e.setCancelled(true);
                gamePlayer.sendMessage(TextFormatter.pluginMessage("Game", "You cannot move this item!"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gamePlayer.updateInventory();
                    }
                }.runTaskLater(ServerAPI.getCore(), 3);
            } else if (gamePlayer.getGameData().containsKey("crystal_possession")) {
                e.setCancelled(true);
                gamePlayer.sendMessage(TextFormatter.pluginMessage("Game", "You cannot move this item!"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gamePlayer.updateInventory();
                    }
                }.runTaskLater(ServerAPI.getCore(), 3);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().name().endsWith("_SWORD") || e.getItemDrop().getItemStack().getType().name().endsWith("_PICKAXE") || e.getItemDrop().getItemStack().getType().name().endsWith("_AXE") || e.getItemDrop().getItemStack().getType() == Material.COMPASS || e.getItemDrop().getItemStack().getType().name().endsWith("_HELMET") || e.getItemDrop().getItemStack().getType().name().endsWith("_CHESTPLATE") || e.getItemDrop().getItemStack().getType().name().endsWith("_LEGGINGS") || e.getItemDrop().getItemStack().getType().name().endsWith("_BOOTS") || e.getItemDrop().getItemStack().getType() == Material.NETHER_STAR || e.getItemDrop().getItemStack().getType() == Material.ARROW || e.getItemDrop().getItemStack().getType() == Material.BOW) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot drop this item!"));
        } else {
            e.setCancelled(!EngineAPI.getActiveGame().isItemDrop());
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.ARROW) {
            e.setCancelled(true);
        } else {
            e.setCancelled(!EngineAPI.getActiveGame().isItemPickup());
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
        if (e.getItem() != null && e.getItem().getType() == Material.COOKIE && e.getItem().getEnchantments().size() == 0) {
            e.setCancelled(true);
            if (e.getItem().getAmount() == 1) {
                e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            } else {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            }
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
            AuroraMCServerPlayer player = e.getPlayer();
            player.getStats().addProgress(AuroraMCAPI.getAchievement(73), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(73), 0), true);
        } else if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (player.getGameData().containsKey("crystal_possession")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use ender pearls while you have a crystal!"));
                return;
            }
            if (player.getGameData().containsKey("last_pearl")) {
                if (System.currentTimeMillis() - (long)player.getGameData().get("last_pearl") < 5000) {
                    double amount = (((long)player.getGameData().get("last_pearl") + 5000) - System.currentTimeMillis()) / 100d;
                    long amount1 = Math.round(amount);
                    if (amount1 < 0) {
                        amount1 = 0;
                    }
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use ender pearls for **" + (amount1 / 10f) + "** seconds!"));
                    return;
                }
            }
            player.getGameData().put("last_pearl", System.currentTimeMillis());

        }

    }

    @EventHandler
    public void onShoot(PlayerShootBowEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME && e.getBow() != null) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (!player.isSpectator() && player.getKit() instanceof Archer && e.getForce() == 1.0f) {
                if (player.getInventory().containsAtLeast(stack, 1)) {
                    Vector v = e.getProjectile().getVelocity();
                    double damage = ((Arrow)e.getProjectile()).spigot().getDamage();
                    e.setCancelled(false);
                    e.getProjectile().remove();
                    Arrow arrow = player.launchProjectile(Arrow.class, v);
                    e.setProjectile(arrow);
                    arrow.spigot().setDamage(damage);
                    player.playSound(player.getEyeLocation(), Sound.SHOOT_ARROW, 1, 100);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Arrow arrow = player.launchProjectile(Arrow.class, v.clone().add(new Vector(0.05, 0, 0.05)));
                            arrow.spigot().setDamage(damage);
                            player.playSound(player.getEyeLocation(), Sound.SHOOT_ARROW, 1, 100);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Arrow arrow = player.launchProjectile(Arrow.class, v.clone().add(new Vector(-0.05, 0, -0.05)));
                                        arrow.spigot().setDamage(damage);
                                        player.playSound(player.getEyeLocation(), Sound.SHOOT_ARROW, 1, 100);
                                    }
                                }.runTaskLater(EngineAPI.getGameEngine(), 2);
                        }
                    }.runTaskLater(EngineAPI.getGameEngine(), 2);
                }

            }
        }
    }

}

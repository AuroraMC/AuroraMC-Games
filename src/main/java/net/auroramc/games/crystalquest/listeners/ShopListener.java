/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.events.entity.EntityDamageByPlayerEvent;
import net.auroramc.core.api.events.inventory.InventoryOpenEvent;
import net.auroramc.core.api.events.player.PlayerArmorStandManipulateEvent;
import net.auroramc.core.api.events.player.PlayerInteractAtEntityEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.gui.PlayerShop;
import net.auroramc.games.crystalquest.gui.TeamShop;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.MerchantInventory;

public class ShopListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByPlayerEvent e) {
        GUI gui = null;
        if (e.getDamaged() instanceof Villager) {
            e.setCancelled(true);
            if (((Villager) e.getDamaged()).getProfession() == Villager.Profession.LIBRARIAN) {
                gui = new PlayerShop((AuroraMCGamePlayer) e.getPlayer());
            } else if (((Villager) e.getDamaged()).getProfession() == Villager.Profession.BLACKSMITH) {
                gui = new TeamShop((AuroraMCGamePlayer) e.getPlayer());
            } else {
                return;
            }
        } else if (e.getDamaged() instanceof ArmorStand) {
            e.setCancelled(true);
        }
        if (e.isCancelled() && gui != null) {
            gui.open(e.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Villager) {
            if (e instanceof EntityDamageByEntityEvent) {
                return;
            }
            e.setCancelled(true);
        } else if (e.getEntity() instanceof ArmorStand) {
            if (e instanceof EntityDamageByEntityEvent) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        if (e.getClickedEntity() instanceof Villager || e.getClickedEntity() instanceof ArmorStand) {
            e.setCancelled(true);
            GUI gui = null;
            if (e.getClickedEntity() instanceof ArmorStand) {
                if (e.getClickedEntity().getCustomName() != null) {
                    AuroraMCServerPlayer player = e.getPlayer();
                    if (((AuroraMCGamePlayer)player).isSpectator() || player.isVanished()) {
                        return;
                    }
                    CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
                    CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");
                    if (e.getClickedEntity().equals(red.getRobotSlotA().getEntity())) {
                        if (player.getTeam().equals(red)) {
                            red.getRobotSlotA().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    } else if (e.getClickedEntity().equals(red.getRobotSlotB().getEntity())) {
                        if (player.getTeam().equals(red)) {
                            red.getRobotSlotB().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    } else if (e.getClickedEntity().equals(red.getRobotSlotC().getEntity())) {
                        if (player.getTeam().equals(red)) {
                            red.getRobotSlotC().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    } else if (e.getClickedEntity().equals(blue.getRobotSlotA().getEntity())) {
                        if (player.getTeam().equals(blue)) {
                            blue.getRobotSlotA().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    } else if (e.getClickedEntity().equals(blue.getRobotSlotB().getEntity())) {
                        if (player.getTeam().equals(blue)) {
                            blue.getRobotSlotB().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    } else if (e.getClickedEntity().equals(blue.getRobotSlotC().getEntity())) {
                        if (player.getTeam().equals(blue)) {
                            blue.getRobotSlotC().openGUI(e.getPlayer());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your mining robot!"));
                        }
                    }
                    return;
                } else {
                    return;
                }
            } else if (e.getClickedEntity() instanceof Villager) {
                if (((AuroraMCGamePlayer)e.getPlayer()).isSpectator() || e.getPlayer().isVanished()) {
                    return;
                }
                if (((Villager) e.getClickedEntity()).getProfession() == Villager.Profession.LIBRARIAN) {
                    gui = new PlayerShop((AuroraMCGamePlayer) e.getPlayer());
                } else if (((Villager) e.getClickedEntity()).getProfession() == Villager.Profession.BLACKSMITH) {
                    gui = new TeamShop((AuroraMCGamePlayer) e.getPlayer());
                } else {
                    return;
                }
            }
            if (gui != null) {
                gui.open(e.getPlayer());
            }
        }

    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof MerchantInventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
        }
    }

}

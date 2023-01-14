/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
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
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.MerchantInventory;

public class ShopListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        GUI gui = null;
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Villager) {
                e.setCancelled(true);
                if (((Villager) e.getEntity()).getProfession() == Villager.Profession.LIBRARIAN) {
                    gui = new PlayerShop((AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getDamager()));
                } else if (((Villager) e.getEntity()).getProfession() == Villager.Profession.BLACKSMITH) {
                    gui = new TeamShop((AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getDamager()));
                } else {
                    return;
                }
            } else if (e.getEntity() instanceof ArmorStand) {
                e.setCancelled(true);
            }
        }
        if (e.isCancelled() && gui != null) {
                gui.open(AuroraMCAPI.getPlayer((Player) e.getDamager()));
            AuroraMCAPI.openGUI(AuroraMCAPI.getPlayer((Player) e.getDamager()), gui);
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
        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof ArmorStand) {
            e.setCancelled(true);
            GUI gui = null;
            if (e.getRightClicked() instanceof ArmorStand) {
                if (e.getRightClicked().getCustomName() != null) {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
                        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");
                        if (e.getRightClicked().equals(red.getRobotSlotA().getEntity())) {
                            if (player.getTeam().equals(red)) {
                                red.getRobotSlotA().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        } else if (e.getRightClicked().equals(red.getRobotSlotB().getEntity())) {
                            if (player.getTeam().equals(red)) {
                                red.getRobotSlotB().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        } else if (e.getRightClicked().equals(red.getRobotSlotC().getEntity())) {
                            if (player.getTeam().equals(red)) {
                                red.getRobotSlotC().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        } else if (e.getRightClicked().equals(blue.getRobotSlotA().getEntity())) {
                            if (player.getTeam().equals(blue)) {
                                blue.getRobotSlotA().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        } else if (e.getRightClicked().equals(blue.getRobotSlotB().getEntity())) {
                            if (player.getTeam().equals(blue)) {
                                blue.getRobotSlotB().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        } else if (e.getRightClicked().equals(blue.getRobotSlotC().getEntity())) {
                            if (player.getTeam().equals(blue)) {
                                blue.getRobotSlotC().openGUI(AuroraMCAPI.getPlayer(e.getPlayer()));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "This is not your mining robot!"));
                            }
                        }
                        return;
                } else {
                    return;
                }
            } else if (e.getRightClicked() instanceof Villager) {
                if (((Villager) e.getRightClicked()).getProfession() == Villager.Profession.LIBRARIAN) {
                    gui = new PlayerShop((AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer()));
                } else if (((Villager) e.getRightClicked()).getProfession() == Villager.Profession.BLACKSMITH) {
                    gui = new TeamShop((AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer()));
                } else {
                    return;
                }
            }
            if (gui != null) {
                gui.open(AuroraMCAPI.getPlayer(e.getPlayer()));
                AuroraMCAPI.openGUI(AuroraMCAPI.getPlayer(e.getPlayer()), gui);
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

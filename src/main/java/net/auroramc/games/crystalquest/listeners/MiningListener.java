/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MiningListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        int protectionRadius = 25;

        if (e.getBlock().getLocation().distanceSquared(red.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotC().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot break blocks here!"));
            return;
        }
        switch (e.getBlock().getType()) {
            case EMERALD_ORE: {
                e.setCancelled(true);
                Location location = e.getBlock().getLocation().clone();
                location.setY(location.getY() + 1);
                if (!location.getBlock().isEmpty()) {
                    location.setY(location.getY() - 1);
                    location.setX(location.getX() + 1);
                    if (!location.getBlock().isEmpty()) {
                        location.setX(location.getX() - 2);
                        if (!location.getBlock().isEmpty()) {
                            location.setX(location.getX() + 1);
                            location.setZ(location.getZ() + 1);
                            if (!location.getBlock().isEmpty()) {
                                location.setZ(location.getZ() - 2);
                                if (!location.getBlock().isEmpty()) {
                                    location.setZ(location.getZ() + 1);
                                }
                            }
                        }
                    }
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.EMERALD));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case IRON_ORE: {
                e.setCancelled(true);
                Location location = e.getBlock().getLocation().clone();
                location.setY(location.getY() + 1);
                if (!location.getBlock().isEmpty()) {
                    location.setY(location.getY() - 1);
                    location.setX(location.getX() + 1);
                    if (!location.getBlock().isEmpty()) {
                        location.setX(location.getX() - 2);
                        if (!location.getBlock().isEmpty()) {
                            location.setX(location.getX() + 1);
                            location.setZ(location.getZ() + 1);
                            if (!location.getBlock().isEmpty()) {
                                location.setZ(location.getZ() - 2);
                                if (!location.getBlock().isEmpty()) {
                                    location.setZ(location.getZ() + 1);
                                }
                            }
                        }
                    }
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.IRON_INGOT));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case GOLD_ORE: {
                e.setCancelled(true);
                Location location = e.getBlock().getLocation().clone();
                location.setY(location.getY() + 1);
                if (!location.getBlock().isEmpty()) {
                    location.setY(location.getY() - 1);
                    location.setX(location.getX() + 1);
                    if (!location.getBlock().isEmpty()) {
                        location.setX(location.getX() - 2);
                        if (!location.getBlock().isEmpty()) {
                            location.setX(location.getX() + 1);
                            location.setZ(location.getZ() + 1);
                            if (!location.getBlock().isEmpty()) {
                                location.setZ(location.getZ() - 2);
                                if (!location.getBlock().isEmpty()) {
                                    location.setZ(location.getZ() + 1);
                                }
                            }
                        }
                    }
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.GOLD_INGOT));
                e.getBlock().setType(Material.STONE);
                break;
            }
            case STONE: {
                e.setCancelled(true);
                if (e.getBlock().getData() != 0) {
                    return;
                }
                Location location = e.getBlock().getLocation().clone();
                location.setY(location.getY() + 1);
                if (!location.getBlock().isEmpty()) {
                    location.setY(location.getY() - 1);
                    location.setX(location.getX() + 1);
                    if (!location.getBlock().isEmpty()) {
                        location.setX(location.getX() - 2);
                        if (!location.getBlock().isEmpty()) {
                            location.setX(location.getX() + 1);
                            location.setZ(location.getZ() + 1);
                            if (!location.getBlock().isEmpty()) {
                                location.setZ(location.getZ() - 2);
                                if (!location.getBlock().isEmpty()) {
                                    location.setZ(location.getZ() + 1);
                                }
                            }
                        }
                    }
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.COBBLESTONE));
                e.getBlock().setType(Material.BEDROCK);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.getBlock().getType() == Material.BEDROCK) {
                            e.getBlock().setType(Material.STONE);
                        }
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 100);
                break;
            }
            case STAINED_CLAY:
            case WOOL: {
                if (e.getBlock().getData() != 14 && e.getBlock().getData() != 11) {
                    e.setCancelled(true);
                    return;
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new GUIItem(e.getBlock().getType(), null, 1, null, (short)((AuroraMCAPI.getPlayer(e.getPlayer()).getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItem());
                break;
            }
            case COBBLESTONE:
            case WOOD:
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new GUIItem(e.getBlock().getType()).getItem());
                break;
            case STAINED_GLASS: {
                if (e.getBlock().getData() != 14 && e.getBlock().getData() != 11) {
                    e.setCancelled(true);
                    return;
                }
                break;
            }
            default: {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            if (e.getEntity() instanceof Player && e.getFoodLevel() < 25) {
                e.setCancelled(true);
                e.setFoodLevel(30);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        int protectionRadius = 25;
        if (e.getBlock().getLocation().distanceSquared(red.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotC().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot place blocks here!"));
        }
    }

}

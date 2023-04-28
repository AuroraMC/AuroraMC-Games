/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.core.api.events.block.BlockPlaceEvent;
import net.auroramc.core.api.events.entity.FoodLevelChangeEvent;
import net.auroramc.core.api.events.player.PlayerPickupItemEvent;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.players.PlayerKitLevel;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.kits.Miner;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class MiningListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (EngineAPI.getActiveGame().isStarting() || ((AuroraMCGamePlayer)e.getPlayer()).isSpectator() || e.getPlayer().isVanished()) {
            e.setCancelled(true);
            return;
        }

        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        int protectionRadius = 25;

        if (e.getBlock().getLocation().distanceSquared(red.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotC().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getTowerACrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getTowerBCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getBossCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getTowerACrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getTowerBCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getBossCrystal().getHome()) < protectionRadius) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot break blocks here!"));
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
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
                int amount = 1;
                if (player.getKit() instanceof Miner) {
                    PlayerKitLevel level = player.getKitLevel();
                    int rand = new Random().nextInt(100);
                    switch (level.getLatestUpgrade()) {
                        case 1: {
                            if (rand < 3) {
                                amount = 2;
                            }
                            break;
                        }
                        case 2: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 6) {
                                amount = 2;
                            }
                            break;
                        }
                        case 3: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 9) {
                                amount = 2;
                            }
                            break;
                        }
                        case 4: {
                            if (rand < 4) {
                                amount = 3;
                            } else if (rand < 12) {
                                amount = 2;
                            }
                            break;
                        }
                        case 5:{
                            if (rand < 6) {
                                amount = 3;
                            } else if (rand < 15) {
                                amount = 2;
                            }
                            break;
                        }
                    }
                }
                Map<Integer, ItemStack> stacks = e.getPlayer().getInventory().addItem(new ItemStack(Material.EMERALD, amount));
                if (stacks.size() > 0) {
                    e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.EMERALD, stacks.get(0).getAmount()));
                }
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
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
                int amount = 1;
                if (player.getKit() instanceof Miner) {
                    PlayerKitLevel level = player.getKitLevel();
                    int rand = new Random().nextInt(100);
                    switch (level.getLatestUpgrade()) {
                        case 1: {
                            if (rand < 3) {
                                amount = 2;
                            }
                            break;
                        }
                        case 2: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 6) {
                                amount = 2;
                            }
                            break;
                        }
                        case 3: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 9) {
                                amount = 2;
                            }
                            break;
                        }
                        case 4: {
                            if (rand < 4) {
                                amount = 3;
                            } else if (rand < 12) {
                                amount = 2;
                            }
                            break;
                        }
                        case 5:{
                            if (rand < 6) {
                                amount = 3;
                            } else if (rand < 15) {
                                amount = 2;
                            }
                            break;
                        }
                    }
                }
                Map<Integer, ItemStack> stacks = e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, amount));
                if (stacks.size() > 0) {
                    e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.IRON_INGOT, stacks.get(0).getAmount()));
                }
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
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
                int amount = 1;
                if (player.getKit() instanceof Miner) {
                    PlayerKitLevel level = player.getKitLevel();
                    int rand = new Random().nextInt(100);
                    switch (level.getLatestUpgrade()) {
                        case 1: {
                            if (rand < 3) {
                                amount = 2;
                            }
                            break;
                        }
                        case 2: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 6) {
                                amount = 2;
                            }
                            break;
                        }
                        case 3: {
                            if (rand < 2) {
                                amount = 3;
                            } else if (rand < 9) {
                                amount = 2;
                            }
                            break;
                        }
                        case 4: {
                            if (rand < 4) {
                                amount = 3;
                            } else if (rand < 12) {
                                amount = 2;
                            }
                            break;
                        }
                        case 5:{
                            if (rand < 6) {
                                amount = 3;
                            } else if (rand < 15) {
                                amount = 2;
                            }
                            break;
                        }
                    }
                }
                Map<Integer, ItemStack> stacks = e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amount));
                if (stacks.size() > 0) {
                    e.getBlock().getLocation().getWorld().dropItemNaturally(location, new ItemStack(Material.GOLD_INGOT, stacks.get(0).getAmount()));
                }
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
                }.runTaskLater(ServerAPI.getCore(), 100);
                break;
            }
            case STAINED_CLAY:
            case WOOL: {
                if (e.getBlock().getData() != 14 && e.getBlock().getData() != 11) {
                    e.setCancelled(true);
                    return;
                }
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new GUIItem(e.getBlock().getType(), null, 1, null, (short)((e.getPlayer().getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItemStack());
                break;
            }
            case COBBLESTONE:
            case WOOD:
            case ENDER_STONE:
            case OBSIDIAN:
            case LADDER:
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(e.getBlock().getType()));
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
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (!player.getGameData().containsKey("crystal_possession")) {
                if (e.getLevel() < 25) {
                    e.setCancelled(true);
                    e.setLevel(30);
                }
            } else {
                if (e.getLevel() < 3) {
                    e.setCancelled(true);
                    e.setLevel(3);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        int protectionRadius = 25;
        if (e.getBlock().getLocation().distanceSquared(red.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getRobotSlotC().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotA().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getRobotSlotB().getLocation()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getTowerACrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getTowerBCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(blue.getBossCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getTowerACrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getTowerBCrystal().getHome()) < protectionRadius || e.getBlock().getLocation().distanceSquared(red.getBossCrystal().getHome()) < protectionRadius) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot place blocks here!"));
        }

        JSONArray redSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("RED");
        JSONArray blueSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("BLUE");
        if (spawnCheck(e, redSpawns)) return;
        if (spawnCheck(e, blueSpawns)) return;

        if (!e.isCancelled()) {
            if (e.getBlockAgainst().getType() == Material.BARRIER) {
                e.setCancelled(true);
            }
        }
    }

    private boolean spawnCheck(BlockPlaceEvent e, JSONArray blueSpawns) {
        for (Object obj : blueSpawns) {
            JSONObject spawn = (JSONObject) obj;
            int x, y, z;
            x = spawn.getInt("x");
            y = spawn.getInt("y");
            z = spawn.getInt("z");
            float yaw = spawn.getFloat("yaw");
            Location location = new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0);
            if (location.distanceSquared(e.getBlock().getLocation()) < 64) {
                e.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.ARROW) {
            e.getItem().setItemStack(new ItemStack(Material.ARROW, e.getItem().getItemStack().getAmount()));
        }
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        if (e.getBlock().isLiquid()) {
            e.setCancelled(true);
        }
    }

}

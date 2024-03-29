/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.gui;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class Shop extends GUI {

    private AuroraMCServerPlayer player;

    public Shop(AuroraMCServerPlayer player) {
        super("&3&lPaintball Shop", 2, true);
        border("&3&lPaintball Shop", null);
        this.player = player;

        this.setItem(1, 1, new GUIItem(Material.WHEAT, "&a&l+3 Lives", 1, ";&fAdd 3 lives to your teams total.;;&r&fCost: &610 Gold;&aClick to buy!"));
        this.setItem(1, 2, new GUIItem(Material.ANVIL, "&a&lMore Ammo!", 1, ";&fReceive 32 more snowballs for use;&fon the battlefield!.;;&r&fCost: &612 Gold;&aClick to buy!"));
        this.setItem(1, 3, new GUIItem(Material.ANVIL, "&a&lTeam Ammo!", 2, ";&fEveryone on your team will receive 32 more snowballs for use;&r&fon the battlefield!.;;&r&fCost: &624 Gold;&aClick to buy!"));

        this.setItem(1, 5, new GUIItem(Material.FIREWORK, "&c&lMissle Strike!", 1, ";&fSummon a Missle Strike that attacks;&fanyone on the battlefield!.;;&cNOTE:&f This item is capable of;&r&ffriendly fire! You do not;&r&freceive gold for using this item.;;&rCost: &624 Gold;&aClick to buy!"));
        this.setItem(1, 6, new GUIItem(Material.EGG, "&c&lFlashbang", 1, ";&r&fWhen you throw this flashbang, it will blind;&r&fany player within 10 blocks of where it lands!;;&r&fCost: &612 Gold;&aClick to buy!"));
        this.setItem(1, 7, new GUIItem(Material.GOLD_BARDING, "&c&lTurret", 1, ";&r&fSpawn a turret that shoots snowballs;&fat your enemies for you!;;&cNOTE: &r&fThis only lasts 30 seconds.;;&r&fCost: &616 Gold;&aClick to buy!"));
    }

    @Override
    public void onClick(int row, int column, ItemStack itemStack, ClickType clickType) {
        int gold = (int)((AuroraMCGamePlayer) player).getGameData().get("gold");
        switch (column) {
            case 1: {
                if (gold >= 10) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "extraLives", 1, true);
                    if (player.getTeam() instanceof PBBlue) {
                        PBBlue blue = (PBBlue) player.getTeam();
                        blue.plus3Lives();
                    } else {
                        PBRed red = (PBRed) player.getTeam();
                        red.plus3Lives();
                    }
                    gold -= 10;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 2: {
                if (gold >= 12) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "extraAmmo", 1, true);
                    int amountLeft = 32;
                    for (int i = 0;i < 8;i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack != null) {
                            if (stack.getType() == Material.SNOW_BALL) {
                                if (stack.getAmount() < 64) {
                                    if (stack.getAmount() + amountLeft > 64) {
                                        amountLeft -= (64-stack.getAmount());
                                        player.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, 64).getItemStack());
                                    } else {
                                        player.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, stack.getAmount() + amountLeft).getItemStack());
                                        break;
                                    }
                                }
                            }
                        } else {
                            player.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, amountLeft).getItemStack());
                            break;
                        }
                    }
                    gold -= 12;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 3: {
                if (gold >= 24) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "teamAmmo", 1, true);
                    for (AuroraMCServerPlayer pl : ServerAPI.getPlayers().stream().filter(pl -> !pl.isDead() && !((AuroraMCGamePlayer)pl).isSpectator() && pl.getTeam() != null && pl.getTeam().equals(player.getTeam())).collect(Collectors.toList())) {
                        int amountLeft = 32;
                        for (int i = 0;i < 8;i++) {
                            ItemStack stack = pl.getInventory().getItem(i);
                            if (stack != null) {
                                if (stack.getType() == Material.SNOW_BALL) {
                                    if (stack.getAmount() < 64) {
                                        if (stack.getAmount() + amountLeft > 64) {
                                            amountLeft -= (64-stack.getAmount());
                                            pl.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, 64).getItemStack());
                                        } else {
                                            pl.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, stack.getAmount() + amountLeft).getItemStack());
                                            break;
                                        }
                                    }
                                }
                            } else {
                                pl.getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, amountLeft).getItemStack());
                                break;
                            }
                        }
                        pl.sendMessage(TextFormatter.pluginMessage("Game", "You received **32 snowballs** as **" + player.getName() + "** bought team ammo!"));
                    }
                    gold -= 24;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 5: {
                if (gold >= 24 && !player.getInventory().contains(Material.FIREWORK, 1)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().addItem(new GUIItem(Material.FIREWORK, "&c&lMissile Strike", 1).getItemStack());
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "missileStrike", 1, true);
                    gold -= 24;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else if (player.getInventory().contains(Material.FIREWORK, 1)) {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.sendMessage(TextFormatter.pluginMessage("Game", "You can only have 1 Missile Strike item in your inventory at a time!"));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 6: {
                if (gold >= 12) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().addItem(new GUIItem(Material.EGG, "&c&lFlashbang", 1).getItemStack());
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "flashbang", 1, true);
                    gold -= 12;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 7: {
                if (gold >= 16 && !player.getInventory().contains(Material.GOLD_BARDING, 1)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "turret", 1, true);
                    player.getInventory().addItem(new GUIItem(Material.GOLD_BARDING, "&c&lTurret", 1).getItemStack());
                    gold -= 16;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItemStack());
                } else if (player.getInventory().contains(Material.GOLD_BARDING, 1)) {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.sendMessage(TextFormatter.pluginMessage("Game", "You can only have 1 Turret item in your inventory at a time!"));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
        }
    }
}

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.gui;

import net.auroramc.core.api.players.AuroraMCPlayer;
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

public class Shop extends GUI {

    private AuroraMCPlayer player;

    public Shop(AuroraMCPlayer player) {
        super("&3&lPaintball Shop", 3, true);
        border("&3&lPaintball Shop", null);
        this.player = player;

        this.setItem(1, 1, new GUIItem(Material.WHEAT, "&a&l+3 Lives", 1, ";&7Add 3 lives to your teams total.;;&r&fCost: &610 Gold;&aClick to buy!"));
        this.setItem(1, 3, new GUIItem(Material.ANVIL, "&a&lMore Ammo!", 1, ";&7Receive 32 more snowballs for use;&7on the battlefield!.;;&r&fCost: &612 Gold;&aClick to buy!"));
        this.setItem(1, 5, new GUIItem(Material.EGG, "&c&lFlashbang", 1, ";&7When you throw this flashbang, it will blind;&r&fany player within 10 blocks of where it lands!;;&r&fCost: &612 Gold;&aClick to buy!"));
        this.setItem(1, 7, new GUIItem(Material.GOLD_BARDING, "&c&lTurret", 1, ";&7Spawn a turret that shoots snowballs;&7at your enemies for you!;;&cNote: &r&fThis only lasts 30 seconds.;;&r&fCost: &616 Gold;&aClick to buy!"));
    }

    @Override
    public void onClick(int row, int column, ItemStack itemStack, ClickType clickType) {
        int gold = (int)((AuroraMCGamePlayer) player).getGameData().get("gold");
        switch (column) {
            case 1: {
                if (gold >= 10) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "extraLives", 1, true);
                    if (player.getTeam() instanceof PBBlue) {
                        PBBlue blue = (PBBlue) player.getTeam();
                        blue.addLife();
                        blue.addLife();
                        blue.addLife();
                    } else {
                        PBRed red = (PBRed) player.getTeam();
                        red.addLife();
                        red.addLife();
                        red.addLife();
                    }
                    gold -= 10;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItem());
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 3: {
                if (gold >= 12) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "extraAmmo", 1, true);
                    int amountLeft = 32;
                    for (int i = 0;i < 8;i++) {
                        ItemStack stack = player.getPlayer().getInventory().getItem(i);
                        if (stack != null) {
                            if (stack.getType() == Material.SNOW_BALL) {
                                if (stack.getAmount() < 64) {
                                    if (stack.getAmount() + amountLeft > 64) {
                                        amountLeft -= (64-stack.getAmount());
                                        player.getPlayer().getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, 64).getItem());
                                    } else {
                                        player.getPlayer().getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, stack.getAmount() + amountLeft).getItem());
                                        break;
                                    }
                                }
                            }
                        } else {
                            player.getPlayer().getInventory().setItem(i, new GUIItem(Material.SNOW_BALL, null, amountLeft).getItem());
                            break;
                        }
                    }
                    gold -= 12;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItem());
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 5: {
                if (gold >= 12) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().addItem(new GUIItem(Material.EGG, "&c&lFlashbang", 1).getItem());
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "flashbang", 1, true);
                    gold -= 12;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItem());
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case 7: {
                if (gold >= 16 && !player.getPlayer().getInventory().contains(Material.GOLD_BARDING, 1)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "turret", 1, true);
                    player.getPlayer().getInventory().addItem(new GUIItem(Material.GOLD_BARDING, "&c&lTurret", 1).getItem());
                    gold -= 16;
                    ((AuroraMCGamePlayer) player).getGameData().put("gold", gold);
                    int i = gold;
                    if (i == 0) {
                        i = 1;
                    }
                    player.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + gold + " Gold", i).getItem());
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
        }
    }
}

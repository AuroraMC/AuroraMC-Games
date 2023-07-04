/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.games.crystalquest.entities.MiningRobot;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RobotMenu extends GUI {

    private static final Map<Integer, String> descs;

    static {
        descs = new HashMap<>();
        descs.put(1, "&b5 &7Iron &r&fevery &b10 seconds&r&f.;&b3 &6Gold &r&fevery &b20 seconds&r&f.;&b1 &aEmeralds &r&fevery &b30 seconds&r&f.");
        descs.put(2, "&b7 &7Iron &r&fevery &b7 seconds&r&f.;&b4 &6Gold &r&fevery &b14 seconds&r&f.;&b1 &aEmeralds &r&fevery &b20 seconds&r&f.");
        descs.put(3, "&b7 &7Iron &r&fevery &b7 seconds&r&f.;&b6 &6Gold &r&fevery &b7 seconds&r&f.;&b1 &aEmeralds &r&fevery &b14 seconds&r&f.");
    }

    private final MiningRobot robot;
    private final AuroraMCServerPlayer player;

    public RobotMenu(AuroraMCServerPlayer player, MiningRobot robot) {
        super(robot.getEntity().getCustomName() + " (Level " + robot.getLevel() + ")", 2, true);
        border(robot.getEntity().getCustomName() + " (Level " + robot.getLevel() + ")", null);

        this.player = player;
        this.robot = robot;

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, robot.getEntity().getCustomName() + " (Level " + robot.getLevel() + ")", 1, "&r&fThis mines:;" + descs.get(robot.getLevel())));

        this.setItem(1, 2, new GUIItem(Material.SKULL_ITEM, "&3&lPersonal Resources", 1, "&r&fYou have:;;&b" + robot.getInventories().get(player).getIron() + " &7Iron;&b" + robot.getInventories().get(player).getGold() + " &6Gold;;&aClick to collect!", (short)3, false, player.getName()));
        this.setItem(1, 4, new GUIItem(Material.SKULL_ITEM, "&3&lTeam Resources", 1, "&r&fYou have:;;&b" + robot.getEmeralds() + " &aEmeralds;;&aClick to collect!", (short)3));
        if (robot.getLevel() == 3) {
            this.setItem(1, 6, new GUIItem(Material.BARRIER, "&3&lUpgrade Robot", 1, ";&r&fYou already have the max upgrade for this robot."));
        } else {
            this.setItem(1, 6, new GUIItem(Material.EXP_BOTTLE, "&3&lUpgrade Robot", 1, ";&r&fUpgrade to:;&bLevel " + (robot.getLevel() + 1) + ";;&r&fCost:;&b" + ((robot.getLevel() == 1)?24:32) + " &aEmeralds;;&r&fThis mines:;" + descs.get(robot.getLevel() + 1)));
        }

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (row != 1) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        switch (column) {
            case 2: {
                if (robot.getInventories().get(player).getIron() > 0) {
                    Map<Integer, ItemStack> stacks = player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, robot.getInventories().get(player).withdrawIron()));
                    if (stacks.size() > 0) {
                        robot.getInventories().get(player).addIron(stacks.get(0).getAmount());
                    }
                }
                if (robot.getInventories().get(player).getGold() > 0) {
                    Map<Integer, ItemStack> stacks = player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, robot.getInventories().get(player).withdrawGold()));
                    if (stacks.size() > 0) {
                        robot.getInventories().get(player).addGold(stacks.get(0).getAmount());
                    }
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                this.updateItem(1, 2, new GUIItem(Material.SKULL_ITEM, "&3&lPersonal Resources", 1, "&r&fYou have:;;&b" + robot.getInventories().get(player).getIron() + " &7Iron;&b" + robot.getInventories().get(player).getGold() + " &6Gold;;&aClick to collect!", (short)3, false, player.getName()));
                break;
            }
            case 4: {
                if (robot.getEmeralds() > 0) {
                    Map<Integer, ItemStack> stacks = player.getInventory().addItem(new ItemStack(Material.EMERALD, robot.withdrawEmeralds()));
                    if (stacks.size() > 0) {
                        robot.addEmeralds(stacks.get(0).getAmount());
                    }
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                this.updateItem(1, 4, new GUIItem(Material.SKULL_ITEM, "&3&lTeam Resources", 1, "&r&fYou have:;;&b" + robot.getEmeralds() + " &aEmeralds;;&aClick to collect!", (short)3));
                break;
            }
            case 6: {
                if (item.getType() == Material.BARRIER) {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    return;
                }

                if (player.getInventory().contains(Material.EMERALD, ((robot.getLevel() == 1)?24:32))) {
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, ((robot.getLevel() == 1)?24:32)));
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    robot.upgrade();
                    if (robot.getLevel() == 3) {
                        this.setItem(1, 6, new GUIItem(Material.BARRIER, "&3&lUpgrade Robot", 1, ";&r&fYou already have the max upgrade for this robot."));
                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(69))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(69), 1, true);
                        }
                    } else {
                        this.setItem(1, 6, new GUIItem(Material.EXP_BOTTLE, "&3&lUpgrade Robot", 1, ";&r&fUpgrade to:;&bLevel " + (robot.getLevel() + 1) + ";;Cost:;&b" + ((robot.getLevel() == 1)?24:32) + " &aEmeralds;;&r&fThis mines:;" + descs.get(robot.getLevel() + 1)));
                    }
                    player.closeInventory();
                    player.sendMessage(TextFormatter.pluginMessage("Game", "Your mining robot was upgraded to **Level " + robot.getLevel() + "**!"));
                }
                break;
            }
            default: {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        }
    }

    public MiningRobot getRobot() {
        return robot;
    }
}

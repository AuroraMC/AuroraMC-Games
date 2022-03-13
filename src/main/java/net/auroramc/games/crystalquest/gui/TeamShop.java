/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.MiningRobot;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TeamShop extends GUI {

    private static final Map<Integer, String> descs;

    static {
        descs = new HashMap<>();
        descs.put(1, "&b5 &7Iron &revery &b10 seconds&r.;&b3 &6Gold &revery &b20 seconds&r.;&b1 &aEmeralds &revery &b30 seconds&r.");
        descs.put(2, "&b7 &7Iron &revery &b7 seconds&r.;&b4 &6Gold &revery &b14 seconds&r.;&b1 &aEmeralds &revery &b20 seconds&r.");
        descs.put(3, "&b7 &7Iron &revery &b7 seconds&r.;&b6 &6Gold &revery &b7 seconds&r.;&b1 &aEmeralds &revery &b14 seconds&r.");
    }

    private final AuroraMCGamePlayer player;

    public TeamShop(AuroraMCGamePlayer player) {
        super("&3&lTeam Shop", 5, true);
        fill("&3&lTeam Shop", null);

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lTeam Shop", 1, ";&rUpgrades purchased from this;&rshop effect your whole team!", (short)3, false, player.getPlayer().getName()));

        int prot, power, sharp;
        MiningRobot robotA;
        MiningRobot robotB;
        MiningRobot robotC;

        if (player.getTeam() instanceof CQBlue) {
            CQBlue blue = (CQBlue) player.getTeam();
            prot = blue.getProtUpgrade();
            power = blue.getPowerUpgrade();
            sharp = blue.getSharpUpgrade();
            robotA = blue.getRobotSlotA();
            robotB = blue.getRobotSlotB();
            robotC = blue.getRobotSlotC();
        } else {
            CQRed red = (CQRed) player.getTeam();
            prot = red.getProtUpgrade();
            power = red.getPowerUpgrade();
            sharp = red.getSharpUpgrade();
            robotA = red.getRobotSlotA();
            robotB = red.getRobotSlotB();
            robotC = red.getRobotSlotC();
        }

        if (prot == 2) {
            this.setItem(1, 1, new GUIItem(Material.BARRIER, "&3&lProtection Upgrade", 1, ";&rYou already have the max upgrade."));
        } else {
            this.setItem(1, 1, new GUIItem(Material.NETHER_STAR, "&3&lProtection Upgrade", 1, ";&rCurrent: **" + ((prot == 0)?"None":"Level " + prot) + "**;;&rClick to upgrade to:;**Protection " + (prot + 1) + "**;&rCost: &b" + ((prot == 0)?16:20) + " &aEmeralds"));
        }

        if (power == 2) {
            this.setItem(1, 3, new GUIItem(Material.BARRIER, "&3&lPower Upgrade", 1, ";&rYou already have the max upgrade."));
        } else {
            this.setItem(1, 3, new GUIItem(Material.NETHER_STAR, "&3&lPower Upgrade", 1, ";&rCurrent: **" + ((power == 0)?"None":"Level " + power) + "**;;&rClick to upgrade to:;**Power " + (power + 1) + "**;&rCost: &b" + ((power == 0)?9:12) + " &aEmeralds"));
        }

        if (sharp == 2) {
            this.setItem(1, 5, new GUIItem(Material.BARRIER, "&3&lSharpness Upgrade", 1, ";&rYou already have the max upgrade."));
        } else {
            this.setItem(1, 5, new GUIItem(Material.NETHER_STAR, "&3&lSharpness Upgrade", 1, ";&rCurrent: **" + ((sharp == 0)?"None":"Level " + sharp) + "**;;&rClick to upgrade to:;**Sharpness " + (sharp + 1) + "**;&rCost: &b" + ((sharp == 0)?13:17) + " &aEmeralds"));
        }

        this.setItem(1, 7, new GUIItem(Material.SKULL_ITEM, "&3&lAdditional Life", 1, ";&rA person on your team can;&rrespawn with all of their items.;;&rCost:;&b48 &7Iron;&b32 &6Gold;&b8 &aEmeralds;;&rPlease note: you can only have 5;&radditional lives at a time."));

        if (robotB.getEntity() != null && robotC.getEntity() != null) {
            this.setItem(2, 4, new GUIItem(Material.BARRIER, "&3&lLevel 1 Mining Robot", 1, ";&rAll of your Mining Robot slots are full!"));
        } else {
            this.setItem(2, 4, new GUIItem(Material.SKULL_ITEM, "&3&lLevel 1 Mining Robot", 1, ";&rThis mines:;" + descs.get(1) + ";;&rCost:;&b16 &aEmeralds"));
        }

        this.setItem(4, 3, new GUIItem(Material.SKULL_ITEM, robotA.getEntity().getCustomName() + " (Level " + robotA.getLevel() + ")", 1, ";&rThis mines:;" + descs.get(robotA.getLevel())));
        if (robotB.getEntity() != null) {
            this.setItem(4, 4, new GUIItem(Material.SKULL_ITEM, robotB.getEntity().getCustomName() + " (Level " + robotB.getLevel() + ")", 1, ";&rThis mines:;" + descs.get(robotB.getLevel())));
        } else {
            this.setItem(4, 4, new GUIItem(Material.BARRIER, "&c&lEmpty Slot", 1, ";&rPurchase a Mining Robot to fill this slot!"));
        }
        if (robotC.getEntity() != null) {
            this.setItem(4, 5, new GUIItem(Material.SKULL_ITEM, robotC.getEntity().getCustomName() + " (Level " + robotC.getLevel() + ")", 1, ";&rThis mines:;" + descs.get(robotC.getLevel())));
        } else {
            this.setItem(4, 5, new GUIItem(Material.BARRIER, "&c&lEmpty Slot", 1, ";&rPurchase a Mining Robot to fill this slot!"));
        }

        this.player = player;
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if ((row != 1 && row != 2) || item.getType() == Material.BARRIER || item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        int prot, power, sharp;
        MiningRobot robotA;
        MiningRobot robotB;
        MiningRobot robotC;

        if (player.getTeam() instanceof CQBlue) {
            prot = blue.getProtUpgrade();
            power = blue.getPowerUpgrade();
            sharp = blue.getSharpUpgrade();
            robotA = blue.getRobotSlotA();
            robotB = blue.getRobotSlotB();
            robotC = blue.getRobotSlotC();
        } else {
            prot = red.getProtUpgrade();
            power = red.getPowerUpgrade();
            sharp = red.getSharpUpgrade();
            robotA = red.getRobotSlotA();
            robotB = red.getRobotSlotB();
            robotC = red.getRobotSlotC();
        }

        switch (column) {
            case 1: {
                if (prot == 2) {
                    this.updateItem(1, 1, new GUIItem(Material.BARRIER, "&3&lProtection Upgrade", 1, ";&rYou already have the max upgrade."));
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    return;
                } else {
                    if (player.getPlayer().getInventory().contains(Material.EMERALD, ((prot == 0)?16:20))) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, ((prot == 0)?16:20)));
                        if (player.getTeam() instanceof CQBlue) {
                            blue.upgradeProt();
                        } else {
                            red.upgradeProt();
                        }
                    }
                    if (player.getTeam() instanceof CQBlue) {
                        prot = blue.getProtUpgrade();
                    } else {
                        prot = red.getProtUpgrade();
                    }
                    if (prot == 2) {
                        this.updateItem(1, 1, new GUIItem(Material.BARRIER, "&3&lProtection Upgrade", 1, ";&rYou already have the max upgrade."));
                    } else {
                        this.updateItem(1, 1, new GUIItem(Material.NETHER_STAR, "&3&lProtection Upgrade", 1, ";&rCurrent: **" + ((prot == 0)?"None":"Level " + prot) + "**;;&rClick to upgrade to:;**Protection " + (prot + 1) + "**;&rCost: &b" + ((prot == 0)?16:20) + " &aEmeralds"));
                    }
                }
                break;
            }
            case 3: {
                if (power == 2) {
                    this.updateItem(1, 3, new GUIItem(Material.BARRIER, "&3&lPower Upgrade", 1, ";&rYou already have the max upgrade."));
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    return;
                } else {
                    if (player.getPlayer().getInventory().contains(Material.EMERALD, ((power == 0)?9:12))) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, ((power == 0)?9:12)));
                        if (player.getTeam() instanceof CQBlue) {
                            blue.upgradePower();
                        } else {
                            red.upgradePower();
                        }
                    }
                    if (player.getTeam() instanceof CQBlue) {
                        power = blue.getPowerUpgrade();
                    } else {
                        power = red.getPowerUpgrade();
                    }
                    if (power == 2) {
                        this.updateItem(1, 3, new GUIItem(Material.BARRIER, "&3&lPower Upgrade", 1, ";&rYou already have the max upgrade."));
                    } else {
                        this.updateItem(1, 3, new GUIItem(Material.NETHER_STAR, "&3&lPower Upgrade", 1, ";&rCurrent: **" + ((power == 0)?"None":"Level " + power) + "**;;&rClick to upgrade to:;**Power " + (power + 1) + "**;&rCost: &b" + ((power == 0)?9:12) + " &aEmeralds"));
                    }
                }
                break;
            }
            case 4: {
                if (player.getPlayer().getInventory().contains(Material.EMERALD, 8) && (robotB.getEntity() == null || robotC.getEntity() == null)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, 8));
                    if (robotB.getEntity() == null) {
                        robotB.spawn();
                        this.updateItem(4, 4, new GUIItem(Material.SKULL_ITEM, robotB.getEntity().getCustomName() + " (Level " + robotB.getLevel() + ")", 1, ";&rThis mines:;" + descs.get(robotB.getLevel())));
                    } else {
                        robotC.spawn();
                        this.setItem(4, 5, new GUIItem(Material.SKULL_ITEM, robotC.getEntity().getCustomName() + " (Level " + robotC.getLevel() + ")", 1, ";&rThis mines:;" + descs.get(robotC.getLevel())));
                    }
                }
                break;
            }
            case 5: {
                if (sharp == 2) {
                    this.setItem(1, 5, new GUIItem(Material.BARRIER, "&3&lSharpness Upgrade", 1, ";&rYou already have the max upgrade."));
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    return;
                } else {
                    if (player.getPlayer().getInventory().contains(Material.EMERALD, ((sharp == 0)?13:17))) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, ((sharp == 0)?13:17)));
                        if (player.getTeam() instanceof CQBlue) {
                            blue.upgradeSharp();
                        } else {
                            red.upgradeSharp();
                        }
                    }
                    if (player.getTeam() instanceof CQBlue) {
                        sharp = blue.getSharpUpgrade();
                    } else {
                        sharp = red.getSharpUpgrade();
                    }
                    if (sharp == 2) {
                        this.setItem(1, 5, new GUIItem(Material.BARRIER, "&3&lSharpness Upgrade", 1, ";&rYou already have the max upgrade."));
                    } else {
                        this.setItem(1, 5, new GUIItem(Material.NETHER_STAR, "&3&lSharpness Upgrade", 1, ";&rCurrent: **" + ((sharp == 0)?"None":"Level " + sharp) + "**;;&rClick to upgrade to:;**Sharpness " + (sharp + 1) + "**;&rCost: &b" + ((sharp == 0)?13:17) + " &aEmeralds"));
                    }
                }
                break;
            }
            case 7: {
                if (player.getPlayer().getInventory().contains(Material.EMERALD, 8) && player.getPlayer().getInventory().contains(Material.IRON_INGOT, 48) && player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 32)){
                    boolean upgrade;
                    if (player.getTeam() instanceof CQBlue) {
                        upgrade = blue.lifeBrought();
                    } else {
                        upgrade = red.lifeBrought();
                    }
                    if (upgrade) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, 8), new ItemStack(Material.IRON_INGOT, 48), new ItemStack(Material.GOLD_INGOT, 32));
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Your team already has 5 lives!"));
                    }
                }
                break;
            }
        }
    }
}

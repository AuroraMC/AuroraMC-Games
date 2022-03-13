/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerShop extends GUI {

    private final AuroraMCGamePlayer player;

    public PlayerShop(AuroraMCGamePlayer player) {
        super("&3&lPlayer Shop", 5, true);
        this.player = player;
        fill("&3&lPlayer Shop", "");

        GUIItem item;
        switch (player.getPlayer().getInventory().getHelmet().getType()) {
            case LEATHER_HELMET: {
                item = new GUIItem(Material.CHAINMAIL_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Chain Helmet;;&rCost: &b5 &7Iron.");
                break;
            }
            case CHAINMAIL_HELMET: {
                item = new GUIItem(Material.IRON_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Iron Helmet;;&rCost: &b10 &6Gold.");
                break;
            }
            case IRON_HELMET: {
                item = new GUIItem(Material.DIAMOND_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Helmet;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Helmet Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 1, item);

        switch (player.getPlayer().getInventory().getChestplate().getType()) {
            case LEATHER_CHESTPLATE: {
                item = new GUIItem(Material.CHAINMAIL_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Chain Chestplate;;&rCost: &b8 &7Iron.");
                break;
            }
            case CHAINMAIL_CHESTPLATE: {
                item = new GUIItem(Material.IRON_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Iron Chestplate;;&rCost: &b16 &6Gold.");
                break;
            }
            case IRON_CHESTPLATE: {
                item = new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Chestplate;;&rCost: &b16 &aEmeralds.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Chestplate Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(2, 1, item);

        switch (player.getPlayer().getInventory().getLeggings().getType()) {
            case LEATHER_LEGGINGS: {
                item = new GUIItem(Material.CHAINMAIL_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Chain Leggings;;&rCost: &b7 &7Iron.");
                break;
            }
            case CHAINMAIL_LEGGINGS: {
                item = new GUIItem(Material.IRON_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Iron Leggings;;&rCost: &b14 &6Gold.");
                break;
            }
            case IRON_LEGGINGS: {
                item = new GUIItem(Material.DIAMOND_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Leggings;;&rCost: &b14 &aEmeralds.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Leggings Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(3, 1, item);

        switch (player.getPlayer().getInventory().getBoots().getType()) {
            case LEATHER_BOOTS: {
                item = new GUIItem(Material.CHAINMAIL_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Chain Boots;;&rCost: &b4 &7Iron.");
                break;
            }
            case CHAINMAIL_BOOTS: {
                item = new GUIItem(Material.IRON_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Iron Boots;;&rCost: &b18 &6Gold.");
                break;
            }
            case IRON_BOOTS: {
                item = new GUIItem(Material.DIAMOND_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Boots;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Boots Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(4, 1, item);


        this.setItem(1, 3, new GUIItem(Material.STAINED_CLAY, "&3&l16 Clay Blocks", 16, ";&rClick to purchase **16** Clay Blocks.;;&rCost: &b20&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));
        this.setItem(2, 3, new GUIItem(Material.WOOL, "&3&l16 Wool Blocks", 16, ";&rClick to purchase **16** Wool Blocks.;;&rCost: &b24&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));
        this.setItem(3, 3, new GUIItem(Material.WOOD, "&3&l16 Wood Blocks", 16, ";&rClick to purchase **16** Wood Blocks.;;&rCost: &b8&6 Gold Ingots"));
        this.setItem(4, 3, new GUIItem(Material.STAINED_GLASS, "&3&l16 Glass Blocks", 16, ";&rClick to purchase **16** Glass Blocks.;;&rCost: &b32&6 Gold Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));

        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (player.getPlayer().getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }

        switch (player.getPlayer().getInventory().getItem(pickSlot).getType()) {
            case STONE_PICKAXE: {
                item = new GUIItem(Material.IRON_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rClick here to upgrade to;&7Iron Pickaxe;;&rCost: &b24 &7Iron.");
                break;
            }
            case IRON_PICKAXE: {
                item = new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Pickaxe;;&rCost: &b16 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Pickaxe Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 5, item);

        switch (player.getPlayer().getInventory().getItem(axeSlot).getType()) {
            case WOOD_AXE: {
                item = new GUIItem(Material.STONE_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Stone Axe;;&rCost: &b4 &7Iron.");
                break;
            }
            case STONE_AXE: {
                item = new GUIItem(Material.IRON_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Iron Axe;;&rCost: &b18 &6Gold.");
                break;
            }
            case IRON_AXE: {
                item = new GUIItem(Material.DIAMOND_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Axe;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Axe Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(2, 5, item);

        if (!player.getPlayer().getInventory().contains(Material.SHEARS)) {
            this.setItem(3, 5, new GUIItem(Material.SHEARS, "&3Shears", 1, ";&rClick here to buy;&7Shears.;;&rCost: &b32 &7Iron."));
        } else {
            this.setItem(3, 5, new GUIItem(Material.BARRIER, "&3Shears", 1, ";&rYou already have Shears."));
        }

        switch (player.getPlayer().getInventory().getItem(swordSlot).getType()) {
            case STONE_SWORD: {
                item = new GUIItem(Material.IRON_SWORD, "&3Sword Upgrade", 1, ";&rClick here to upgrade to;&7Iron Sword;;&rCost: &b16 &7Iron.");
                break;
            }
            case IRON_SWORD: {
                item = new GUIItem(Material.DIAMOND_SWORD, "&3Sword Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Sword;;&rCost: &b32 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Sword Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 7, item);

        if (!player.getPlayer().getInventory().contains(Material.BOW)) {
            this.setItem(2, 7, new GUIItem(Material.BOW, "&3Bow", 1, ";&rClick here to buy;&7Bow.;;&rCost: &b16 &7Iron."));
        } else {
            this.setItem(2, 7, new GUIItem(Material.BARRIER, "&3Bow", 1, ";&rYou already have a Bow."));
        }

        this.setItem(3, 7, new GUIItem(Material.ARROW, "&316 Arrows", 16, ";&rClick here to buy;&716 Arrows.;;&rCost: &b16 &7Iron."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (player.getPlayer().getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }
        switch (item.getType()) {
            //Boots
            case CHAINMAIL_BOOTS: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 4)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 4));
                    player.getPlayer().getInventory().getBoots().setType(Material.CHAINMAIL_BOOTS);
                    this.updateItem(4, 1, new GUIItem(Material.IRON_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Iron Boots;;&rCost: &b18 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_BOOTS: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 18)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 18));
                    player.getPlayer().getInventory().getBoots().setType(Material.IRON_BOOTS);
                    this.updateItem(4, 1, new GUIItem(Material.DIAMOND_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Boots;;&rCost: &b64 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_BOOTS: {
                if (player.getPlayer().getInventory().getBoots().getType() != Material.DIAMOND_BOOTS) {
                    if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 64)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 64));
                        player.getPlayer().getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
                        this.updateItem(4, 1, new GUIItem(Material.BARRIER, "&3Boots Upgrade", 1, ";&rYou have the max upgrade."));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Leggings
            case CHAINMAIL_LEGGINGS: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 7)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 7));
                    player.getPlayer().getInventory().getLeggings().setType(Material.CHAINMAIL_LEGGINGS);
                    this.updateItem(3, 1, new GUIItem(Material.IRON_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Iron Leggings;;&rCost: &b14 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_LEGGINGS: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 14)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 14));
                    player.getPlayer().getInventory().getLeggings().setType(Material.IRON_LEGGINGS);
                    this.updateItem(3, 1, new GUIItem(Material.DIAMOND_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Leggings;;&rCost: &b14 &aEmeralds."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_LEGGINGS: {
                if (player.getPlayer().getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS) {
                    if (player.getPlayer().getInventory().contains(Material.EMERALD, 14)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, 14));
                        player.getPlayer().getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);
                        this.updateItem(3, 1, new GUIItem(Material.BARRIER, "&3Leggings Upgrade", 1, ";&rYou have the max upgrade."));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Chestplate
            case CHAINMAIL_CHESTPLATE: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 8)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 8));
                    player.getPlayer().getInventory().getChestplate().setType(Material.CHAINMAIL_CHESTPLATE);
                    this.updateItem(2, 1, new GUIItem(Material.IRON_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Iron Chestplate;;&rCost: &b16 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_CHESTPLATE: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 16)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 16));
                    player.getPlayer().getInventory().getChestplate().setType(Material.IRON_CHESTPLATE);
                    this.updateItem(2, 1, new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Chestplate;;&rCost: &b16 &aEmeralds."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_CHESTPLATE: {
                if (player.getPlayer().getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE) {
                    if (player.getPlayer().getInventory().contains(Material.EMERALD, 16)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.EMERALD, 16));
                        player.getPlayer().getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
                        this.updateItem(2, 1, new GUIItem(Material.BARRIER, "&3Chestplate Upgrade", 1, ";&rYou have the max upgrade."));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Helmet
            case CHAINMAIL_HELMET: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 5)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 5));
                    player.getPlayer().getInventory().getHelmet().setType(Material.CHAINMAIL_HELMET);
                    this.updateItem(1, 1, new GUIItem(Material.IRON_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Iron Helmet;;&rCost: &b10 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_HELMET: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 10)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 10));
                    player.getPlayer().getInventory().getHelmet().setType(Material.IRON_HELMET);
                    this.updateItem(1, 1, new GUIItem(Material.DIAMOND_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Helmet;;&rCost: &b64 &6Gold."));
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_HELMET: {
                if (player.getPlayer().getInventory().getHelmet().getType() != Material.DIAMOND_HELMET) {
                    if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 64)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 64));
                        player.getPlayer().getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
                        this.updateItem(1, 1, new GUIItem(Material.BARRIER, "&3Helmet Upgrade", 1, ";&rYou have the max upgrade."));
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Blocks
            case STAINED_CLAY: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 24)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 24));
                    Map<Integer, ItemStack> couldntPlace = player.getPlayer().getInventory().addItem(new GUIItem(Material.STAINED_CLAY, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItem());
                    if (couldntPlace.size() > 0) {
                        player.getPlayer().closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getPlayer().getLocation().getWorld().dropItem(player.getPlayer().getLocation(), entry.getValue());
                        }
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory."));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case WOOL: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 20)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 20));
                    Map<Integer, ItemStack> couldntPlace = player.getPlayer().getInventory().addItem(new GUIItem(Material.WOOL, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItem());
                    if (couldntPlace.size() > 0) {
                        player.getPlayer().closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getPlayer().getLocation().getWorld().dropItem(player.getPlayer().getLocation(), entry.getValue());
                        }
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory."));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case STAINED_GLASS: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 32)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 32));
                    Map<Integer, ItemStack> couldntPlace = player.getPlayer().getInventory().addItem(new GUIItem(Material.STAINED_GLASS, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItem());
                    if (couldntPlace.size() > 0) {
                        player.getPlayer().closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getPlayer().getLocation().getWorld().dropItem(player.getPlayer().getLocation(), entry.getValue());
                        }
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case WOOD: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 8)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 8));
                    Map<Integer, ItemStack> couldntPlace = player.getPlayer().getInventory().addItem(new GUIItem(Material.WOOD, null, 16).getItem());
                    if (couldntPlace.size() > 0) {
                        player.getPlayer().closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getPlayer().getLocation().getWorld().dropItem(player.getPlayer().getLocation(), entry.getValue());
                        }
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Tools
            //Pickaxe
            case IRON_PICKAXE: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 24)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 24));
                    this.updateItem(1, 5, new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Pickaxe;;&rCost: &b16 &6Gold."));
                    player.getPlayer().getInventory().getItem(pickSlot).setType(Material.IRON_PICKAXE);
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_PICKAXE: {
                if (player.getPlayer().getInventory().getItem(pickSlot).getType() != Material.DIAMOND_PICKAXE) {
                    if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 16)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 16));
                        this.updateItem(1, 5, new GUIItem(Material.BARRIER, "&3Pickaxe Upgrade", 1, ";&rYou have the max upgrade."));
                        player.getPlayer().getInventory().getItem(pickSlot).setType(Material.DIAMOND_PICKAXE);
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Axe
            case STONE_AXE: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 4)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 4));
                    this.updateItem(2, 5, new GUIItem(Material.IRON_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Iron Axe;;&rCost: &b18 &6Gold."));
                    player.getPlayer().getInventory().getItem(axeSlot).setType(Material.STONE_AXE);
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_AXE: {
                if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 18)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 18));
                    this.updateItem(2, 5, new GUIItem(Material.DIAMOND_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Axe;;&rCost: &b64 &6Gold."));
                    player.getPlayer().getInventory().getItem(axeSlot).setType(Material.IRON_AXE);
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_AXE: {
                if (player.getPlayer().getInventory().getItem(axeSlot).getType() != Material.DIAMOND_AXE) {
                    if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 64)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 64));
                        this.updateItem(2, 5, new GUIItem(Material.BARRIER, "&3Axe Upgrade", 1, ";&rYou have the max upgrade."));
                        player.getPlayer().getInventory().getItem(axeSlot).setType(Material.DIAMOND_AXE);
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                }
                break;
            }

            //Shears
            case SHEARS: {
                if (!player.getPlayer().getInventory().contains(Material.SHEARS)) {
                    if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 32)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 32));
                        player.getPlayer().getInventory().addItem(new GUIItem(Material.SHEARS).getItem());
                        this.updateItem(3, 5, new GUIItem(Material.BARRIER, "&3Shears", 1, ";&rYou already have Shears."));
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.getPlayer().closeInventory();
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "You already have Shears."));
                }
                break;
            }

            //Weapons
            //Sword
            case IRON_SWORD: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 16)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                    this.updateItem(1, 7, new GUIItem(Material.DIAMOND_SWORD, "&3Sword Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Sword;;&rCost: &b32 &6Gold."));
                    player.getPlayer().getInventory().getItem(swordSlot).setType(Material.IRON_SWORD);
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_SWORD: {
                if (player.getPlayer().getInventory().getItem(swordSlot).getType() != Material.DIAMOND_SWORD) {
                    if (player.getPlayer().getInventory().contains(Material.GOLD_INGOT, 32)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 32));
                        this.updateItem(1, 7, new GUIItem(Material.BARRIER, "&3Sword Upgrade", 1, ";&rYou have the max upgrade."));
                        player.getPlayer().getInventory().getItem(swordSlot).setType(Material.DIAMOND_SWORD);
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                }
                break;
            }

            //Bow and Arrows
            case BOW: {
                if (!player.getPlayer().getInventory().contains(Material.BOW)) {
                    if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 16)) {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                        ItemStack stack = new GUIItem(Material.BOW).getItem();
                        if (player.getTeam() instanceof CQBlue) {
                            if (((CQBlue) player.getTeam()).getPowerUpgrade() > 0) {
                                stack.addEnchantment(Enchantment.ARROW_DAMAGE, ((CQBlue) player.getTeam()).getPowerUpgrade());
                            }
                        } else {
                            if (((CQRed) player.getTeam()).getPowerUpgrade() > 0) {
                                stack.addEnchantment(Enchantment.ARROW_DAMAGE, ((CQRed) player.getTeam()).getPowerUpgrade());
                            }
                        }
                        player.getPlayer().getInventory().addItem(stack);
                        this.updateItem(2, 7, new GUIItem(Material.BARRIER, "&3Bow", 1, ";&rYou already have a Bow."));
                    } else {
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.getPlayer().closeInventory();
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "You already have a Bow."));
                }
                break;
            }
            case ARROW: {
                if (player.getPlayer().getInventory().contains(Material.IRON_INGOT, 16)) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getPlayer().getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                    Map<Integer, ItemStack> couldntPlace = player.getPlayer().getInventory().addItem(new GUIItem(Material.ARROW, null, 16).getItem());
                    if (couldntPlace.size() > 0) {
                        player.getPlayer().closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getPlayer().getLocation().getWorld().dropItem(player.getPlayer().getLocation(), entry.getValue());
                        }
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            default: {
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        }
    }
}

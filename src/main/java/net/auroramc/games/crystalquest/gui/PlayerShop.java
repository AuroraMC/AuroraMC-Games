/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PlayerShop extends GUI {

    private AuroraMCGamePlayer player;

    public PlayerShop(AuroraMCGamePlayer player) {
        super("&3&lPlayer Shop", 5, true);
        this.player = player;
        fill("&3&lPlayer Shop", "");

        GUIItem item;
        switch (player.getPlayer().getInventory().getHelmet().getType()) {
            case LEATHER_HELMET: {
                item = new GUIItem(Material.LEATHER_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Chain Helmet;;&rCost: &b5 &7Iron.");
                break;
            }
            case CHAINMAIL_HELMET: {
                item = new GUIItem(Material.CHAINMAIL_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Iron Helmet;;&rCost: &b10 &6Gold.");
                break;
            }
            case IRON_HELMET: {
                item = new GUIItem(Material.IRON_HELMET, "&3Helmet Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Helmet;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_HELMET, "&3Helmet Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 1, item);

        switch (player.getPlayer().getInventory().getChestplate().getType()) {
            case LEATHER_CHESTPLATE: {
                item = new GUIItem(Material.LEATHER_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Chain Chestplate;;&rCost: &b8 &7Iron.");
                break;
            }
            case CHAINMAIL_CHESTPLATE: {
                item = new GUIItem(Material.CHAINMAIL_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Iron Chestplate;;&rCost: &b16 &6Gold.");
                break;
            }
            case IRON_CHESTPLATE: {
                item = new GUIItem(Material.IRON_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Chestplate;;&rCost: &b16 &aEmeralds.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(2, 1, item);

        switch (player.getPlayer().getInventory().getLeggings().getType()) {
            case LEATHER_LEGGINGS: {
                item = new GUIItem(Material.LEATHER_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Chain Leggings;;&rCost: &b7 &7Iron.");
                break;
            }
            case CHAINMAIL_LEGGINGS: {
                item = new GUIItem(Material.CHAINMAIL_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Iron Leggings;;&rCost: &b14 &6Gold.");
                break;
            }
            case IRON_LEGGINGS: {
                item = new GUIItem(Material.IRON_LEGGINGS, "&3Leggings Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Leggings;;&rCost: &b14 &aEmeralds.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_LEGGINGS, "&3Leggings Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(3, 1, item);

        switch (player.getPlayer().getInventory().getBoots().getType()) {
            case LEATHER_BOOTS: {
                item = new GUIItem(Material.LEATHER_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Chain Boots;;&rCost: &b4 &7Iron.");
                break;
            }
            case CHAINMAIL_BOOTS: {
                item = new GUIItem(Material.CHAINMAIL_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Iron Boots;;&rCost: &b18 &6Gold.");
                break;
            }
            case IRON_BOOTS: {
                item = new GUIItem(Material.IRON_BOOTS, "&3Boots Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Boots;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Boots Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(4, 1, item);


        this.setItem(1, 3, new GUIItem(Material.CLAY, "&3&l16 Clay Blocks", 16, ";&rClick to purchase **16** Clay Blocks.;;&rCost: &b20&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));
        this.setItem(2, 3, new GUIItem(Material.WOOL, "&3&l16 Wool Blocks", 16, ";&rClick to purchase **16** Wool Blocks.;;&rCost: &b24&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));
        this.setItem(3, 3, new GUIItem(Material.WOOD, "&3&l16 Wood Blocks", 16, ";&rClick to purchase **16** Wood Blocks.;;&rCost: &b8&6 Gold Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));
        this.setItem(4, 3, new GUIItem(Material.GLASS, "&3&l16 Glass Blocks", 16, ";&rClick to purchase **16** Glass Blocks.;;&rCost: &b32&6 Gold Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)));

        switch (player.getPlayer().getInventory().getItem(1).getType()) {
            case STONE_PICKAXE: {
                item = new GUIItem(Material.STONE_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rClick here to upgrade to;&7Iron Pickaxe;;&rCost: &b24 &7Iron.");
                break;
            }
            case IRON_PICKAXE: {
                item = new GUIItem(Material.IRON_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Pickaxe;;&rCost: &b16 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 5, item);

        switch (player.getPlayer().getInventory().getItem(2).getType()) {
            case WOOD_AXE: {
                item = new GUIItem(Material.WOOD_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Stone Axe;;&rCost: &b4 &7Iron.");
                break;
            }
            case STONE_AXE: {
                item = new GUIItem(Material.STONE_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Iron Axe;;&rCost: &b18 &6Gold.");
                break;
            }
            case IRON_AXE: {
                item = new GUIItem(Material.IRON_AXE, "&3Axe Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Axe;;&rCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_AXE, "&3Axe Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(2, 5, item);

        if (player.getPlayer().getInventory().contains(Material.SHEARS)) {
            this.setItem(3, 5, new GUIItem(Material.SHEARS, "&3Shears", 1, ";&rClick here to buy;&7Shears.;;&rCost: &b32 &7Iron."));
        } else {
            this.setItem(3, 5, new GUIItem(Material.SHEARS, "&3Shears", 1, ";&rYou already have Shears."));
        }

        switch (player.getPlayer().getInventory().getItem(0).getType()) {
            case STONE_SWORD: {
                item = new GUIItem(Material.STONE_SWORD, "&3Sword Upgrade", 1, ";&rClick here to upgrade to;&7Iron Sword;;&rCost: &b16 &7Iron.");
                break;
            }
            case IRON_SWORD: {
                item = new GUIItem(Material.IRON_SWORD, "&3Sword Upgrade", 1, ";&rClick here to upgrade to;&7Diamond Sword;;&rCost: &b32 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.DIAMOND_SWORD, "&3Sword Upgrade", 1, ";&rYou have the max upgrade.");
                break;
            }
        }
        this.setItem(1, 7, item);

        if (player.getPlayer().getInventory().contains(Material.BOW)) {
            this.setItem(2, 7, new GUIItem(Material.BOW, "&3Bow", 1, ";&rClick here to buy;&7Bow.;;&rCost: &b16 &7Iron."));
        } else {
            this.setItem(2, 7, new GUIItem(Material.BOW, "&3Bow", 1, ";&rYou already have a Bow."));
        }

        this.setItem(3, 7, new GUIItem(Material.ARROW, "&316 Arrows", 16, ";&rClick here to buy;&716 Arrows.;;&rCost: &b16 &7Iron."));
    }

    @Override
    public void onClick(int row, int column, ItemStack itemStack, ClickType clickType) {

    }
}

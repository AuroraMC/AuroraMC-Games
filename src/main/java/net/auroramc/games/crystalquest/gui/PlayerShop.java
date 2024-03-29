/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.kits.Archer;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
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
        switch (player.getInventory().getHelmet().getType()) {
            case LEATHER_HELMET: {
                item = new GUIItem(Material.CHAINMAIL_HELMET, "&3Helmet Upgrade", 1, ";&r&fClick here to upgrade to;&7Chain Helmet;;&r&fCost: &b5 &7Iron.");
                break;
            }
            case CHAINMAIL_HELMET: {
                item = new GUIItem(Material.IRON_HELMET, "&3Helmet Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Helmet;;&r&fCost: &b10 &6Gold.");
                break;
            }
            case IRON_HELMET: {
                item = new GUIItem(Material.DIAMOND_HELMET, "&3Helmet Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Helmet;;&r&fCost: &b40 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Helmet Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(1, 4, item);

        switch (player.getInventory().getChestplate().getType()) {
            case LEATHER_CHESTPLATE: {
                item = new GUIItem(Material.CHAINMAIL_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&r&fClick here to upgrade to;&7Chain Chestplate;;&r&fCost: &b8 &7Iron.");
                break;
            }
            case CHAINMAIL_CHESTPLATE: {
                item = new GUIItem(Material.IRON_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Chestplate;;&r&fCost: &b16 &6Gold.");
                break;
            }
            case IRON_CHESTPLATE: {
                item = new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Chestplate;;&r&fCost: &b64 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Chestplate Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(2, 4, item);

        switch (player.getInventory().getLeggings().getType()) {
            case LEATHER_LEGGINGS: {
                item = new GUIItem(Material.CHAINMAIL_LEGGINGS, "&3Leggings Upgrade", 1, ";&r&fClick here to upgrade to;&7Chain Leggings;;&r&fCost: &b7 &7Iron.");
                break;
            }
            case CHAINMAIL_LEGGINGS: {
                item = new GUIItem(Material.IRON_LEGGINGS, "&3Leggings Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Leggings;;&r&fCost: &b14 &6Gold.");
                break;
            }
            case IRON_LEGGINGS: {
                item = new GUIItem(Material.DIAMOND_LEGGINGS, "&3Leggings Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Leggings;;&r&fCost: &b56 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Leggings Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(3, 4, item);

        switch (player.getInventory().getBoots().getType()) {
            case LEATHER_BOOTS: {
                item = new GUIItem(Material.CHAINMAIL_BOOTS, "&3Boots Upgrade", 1, ";&r&fClick here to upgrade to;&7Chain Boots;;&r&fCost: &b4 &7Iron.");
                break;
            }
            case CHAINMAIL_BOOTS: {
                item = new GUIItem(Material.IRON_BOOTS, "&3Boots Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Boots;;&r&fCost: &b8 &6Gold.");
                break;
            }
            case IRON_BOOTS: {
                item = new GUIItem(Material.DIAMOND_BOOTS, "&3Boots Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Boots;;&r&fCost: &b40 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Boots Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(4, 4, item);

        item = new GUIItem(Material.STAINED_CLAY, "&3&l16 Clay Blocks", 16, ";&r&fClick to purchase **16** &fClay Blocks.;;&r&fCost: &b16&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11));
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(1, 1, item);

        item = new GUIItem(Material.WOOL, "&3&l16 Wool Blocks", 16, ";&r&fClick to purchase **16** &fWool Blocks.;;&r&fCost: &b12&7 Iron Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11));
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(0, 1, item);

        item = new GUIItem(Material.WOOD, "&3&l16 Wood Blocks", 16, ";&r&fClick to purchase **16** &fWood Blocks.;;&r&fCost: &b8&6 Gold Ingots");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(3, 1, item);

        item = new GUIItem(Material.STAINED_GLASS, "&3&l16 Glass Blocks", 16, ";&r&fClick to purchase **16** &fGlass Blocks.;;&r&fCost: &b16&6 Gold Ingots", (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11));
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(2, 1, item);

        item = new GUIItem(Material.ENDER_STONE, "&3&l8 End Stone", 8, ";&r&fClick to purchase **8** &fEnd Stone.;;&r&fCost: &b24&6 Gold Ingots");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(4, 1, item);

        item = new GUIItem(Material.OBSIDIAN, "&3&l1 Obsidian", 1, ";&r&fClick to purchase **1** &fObsidian.;;&r&fCost: &b4&a Emeralds");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(5, 1, item);

        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }

        switch (player.getInventory().getItem(pickSlot).getType()) {
            case STONE_PICKAXE: {
                item = new GUIItem(Material.IRON_PICKAXE, "&3Pickaxe Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Pickaxe;;&r&fCost: &b12 &7Iron.");
                break;
            }
            case IRON_PICKAXE: {
                item = new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Pickaxe;;&r&fCost: &b12 &6Gold.");
                break;
            }
            case DIAMOND_PICKAXE: {
                if (player.getInventory().getItem(pickSlot).getEnchantmentLevel(Enchantment.DIG_SPEED) >= 2) {
                    item = new GUIItem(Material.BARRIER, "&3Pickaxe Upgrade", 1, ";&r&fYou have the max upgrade.");
                } else {
                    item = new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Pickaxe Efficiency II;;&r&fCost: &b32 &6Gold.");
                }
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Pickaxe Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(2, 5, item);

        switch (player.getInventory().getItem(axeSlot).getType()) {
            case WOOD_AXE: {
                item = new GUIItem(Material.STONE_AXE, "&3Axe Upgrade", 1, ";&r&fClick here to upgrade to;&7Stone Axe;;&r&fCost: &b8 &7Iron.");
                break;
            }
            case STONE_AXE: {
                item = new GUIItem(Material.IRON_AXE, "&3Axe Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Axe;;&r&fCost: &b12 &6Gold.");
                break;
            }
            case IRON_AXE: {
                item = new GUIItem(Material.DIAMOND_AXE, "&3Axe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Axe;;&r&fCost: &b32 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Axe Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(3, 5, item);

        if (!player.getInventory().contains(Material.SHEARS)) {
            item = new GUIItem(Material.SHEARS, "&3Shears", 1, ";&r&fClick here to buy;&7Shears.;;&r&fCost: &b16 &7Iron.");
            if (EngineAPI.getActiveGame().getGameVariation() != null) {
                item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
            }
            this.setItem(4, 5, item);
        } else {
            this.setItem(4, 5, new GUIItem(Material.BARRIER, "&3Shears", 1, ";&r&fYou already have Shears."));
        }

        switch (player.getInventory().getItem(swordSlot).getType()) {
            case STONE_SWORD: {
                item = new GUIItem(Material.IRON_SWORD, "&3Sword Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Sword;;&r&fCost: &b16 &7Iron.");
                break;
            }
            case IRON_SWORD: {
                item = new GUIItem(Material.DIAMOND_SWORD, "&3Sword Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Sword;;&r&fCost: &b32 &6Gold.");
                break;
            }
            default: {
                item = new GUIItem(Material.BARRIER, "&3Sword Upgrade", 1, ";&r&fYou have the max upgrade.");
                break;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(2, 3, item);

        item = new GUIItem(Material.GOLD_INGOT, "&3Gold Ingot Conversion", 1, ";&r&fClick here to buy;&61 Gold Ingot.;;&r&fCost: &b20 &7Iron.");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(1, 5, item);
        item = new GUIItem(Material.IRON_INGOT, "&3Iron Ingot Conversion", 1, ";&r&fClick here to buy;&720 Iron Ingots.;;&r&fCost: &b1 &6Gold.");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(1, 3, item);

        if (!player.getInventory().contains(Material.BOW)) {
            item = new GUIItem(Material.BOW, "&3Bow", 1, ";&r&fClick here to buy;&7Bow.;;&r&fCost: &b16 &7Iron.");
            if (EngineAPI.getActiveGame().getGameVariation() != null) {
                item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
            }
            this.setItem(3, 3, item);
        } else {
            this.setItem(3, 3, new GUIItem(Material.BARRIER, "&3Bow", 1, ";&r&fYou already have a Bow."));
        }

        if (!(player.getKit() instanceof Archer)) {
            item = new GUIItem(Material.ARROW, "&32 Arrows", 2, ";&r&fClick here to buy;&72 Arrows.;;&r&fCost: &b8 &7Iron.");
            if (EngineAPI.getActiveGame().getGameVariation() != null) {
                item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
            }
            this.setItem(4, 3, item);
        } else {
            this.setItem(4, 3, new GUIItem(Material.BARRIER, "&32 Arrows", 1, ";&r&fYou are not able to buy arrows with Archer Kit enabled."));
        }

        item = new GUIItem(Material.WATER_BUCKET, "&3&lWater Bucket", 1, ";&r&fClick to purchase **Water Bucket**.;;&r&fCost: &b48&7 Iron");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(0, 7, item);

        item = new GUIItem(Material.GOLDEN_APPLE, "&3&lGolden Apple", 1, ";&r&fClick to purchase **Golden Apple**.;;&r&fCost: &b8&6 Gold");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(1, 7, item);

        item = new GUIItem(Material.COOKIE, "&3&lInstant Cookie", 1, ";&r&fClick to purchase **Instant Cookie**.;;&r&fCost: &b2&a Emerald");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(2, 7, item);

        item = new GUIItem(Material.FLINT_AND_STEEL, "&3&lFlint and Steel", 1, ";&r&fClick to purchase **Flint and Steel**.;;&r&fCost: &b2&a Emeralds");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(3, 7, item);

        item = new GUIItem(Material.ENDER_PEARL, "&3&lEthereal Pearl", 1, ";&r&fClick to purchase **Ethereal Pearl**.;;&r&fCost: &b8&a Emeralds");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(4, 7, item);

        item = new GUIItem(Material.LADDER, "&3&l8 Ladders", 8, ";&r&fClick to purchase **8** &fLadders.;;&r&fCost: &b10&7 Iron");
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            item = ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopDisplayItem(item);
        }
        this.setItem(5, 7, item);
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            if (!((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onShopPurchase(item.getType())) {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                return;
            }
        }
        switch (item.getType()) {
            case IRON_INGOT: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 1)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 20));
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case GOLD_INGOT: {
                if (player.getInventory().contains(Material.IRON_INGOT, 20)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 20));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            //Boots
            case CHAINMAIL_BOOTS: {
                if (player.getInventory().contains(Material.IRON_INGOT, 4)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 4));
                    player.getInventory().getBoots().setType(Material.CHAINMAIL_BOOTS);
                    this.updateItem(4, 4, new GUIItem(Material.IRON_BOOTS, "&3Boots Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Boots;;&r&fCost: &b8 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_BOOTS: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 8));
                    player.getInventory().getBoots().setType(Material.IRON_BOOTS);
                    this.updateItem(4, 4, new GUIItem(Material.DIAMOND_BOOTS, "&3Boots Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Boots;;&r&fCost: &b40 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_BOOTS: {
                if (player.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 40)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 40));
                        player.getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
                        this.updateItem(4, 4, new GUIItem(Material.BARRIER, "&3Boots Upgrade", 1, ";&r&fYou have the max upgrade."));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Leggings
            case CHAINMAIL_LEGGINGS: {
                if (player.getInventory().contains(Material.IRON_INGOT, 7)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 7));
                    player.getInventory().getLeggings().setType(Material.CHAINMAIL_LEGGINGS);
                    this.updateItem(3, 4, new GUIItem(Material.IRON_LEGGINGS, "&3Leggings Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Leggings;;&r&fCost: &b14 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_LEGGINGS: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 14)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 14));
                    player.getInventory().getLeggings().setType(Material.IRON_LEGGINGS);
                    this.updateItem(3, 4, new GUIItem(Material.DIAMOND_LEGGINGS, "&3Leggings Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Leggings;;&r&fCost: &b56 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_LEGGINGS: {
                if (player.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 56)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 56));
                        player.getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);
                        this.updateItem(3, 4, new GUIItem(Material.BARRIER, "&3Leggings Upgrade", 1, ";&r&fYou have the max upgrade."));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Chestplate
            case CHAINMAIL_CHESTPLATE: {
                if (player.getInventory().contains(Material.IRON_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 8));
                    player.getInventory().getChestplate().setType(Material.CHAINMAIL_CHESTPLATE);
                    this.updateItem(2, 4, new GUIItem(Material.IRON_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Chestplate;;&r&fCost: &b16 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_CHESTPLATE: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 16)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 16));
                    player.getInventory().getChestplate().setType(Material.IRON_CHESTPLATE);
                    this.updateItem(2, 4, new GUIItem(Material.DIAMOND_CHESTPLATE, "&3Chestplate Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Chestplate;;&r&fCost: &b64 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_CHESTPLATE: {
                if (player.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 64)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 64));
                        player.getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
                        this.updateItem(2, 4, new GUIItem(Material.BARRIER, "&3Chestplate Upgrade", 1, ";&r&fYou have the max upgrade."));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Helmet
            case CHAINMAIL_HELMET: {
                if (player.getInventory().contains(Material.IRON_INGOT, 5)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 5));
                    player.getInventory().getHelmet().setType(Material.CHAINMAIL_HELMET);
                    this.updateItem(1, 4, new GUIItem(Material.IRON_HELMET, "&3Helmet Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Helmet;;&r&fCost: &b10 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_HELMET: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 10)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 10));
                    player.getInventory().getHelmet().setType(Material.IRON_HELMET);
                    this.updateItem(1, 4, new GUIItem(Material.DIAMOND_HELMET, "&3Helmet Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Helmet;;&r&fCost: &b40 &6Gold."));
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_HELMET: {
                if (player.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 40)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 40));
                        player.getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
                        this.updateItem(1, 4, new GUIItem(Material.BARRIER, "&3Helmet Upgrade", 1, ";&r&fYou have the max upgrade."));
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Blocks
            case STAINED_CLAY: {
                if (player.getInventory().contains(Material.IRON_INGOT, 16)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.STAINED_CLAY, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory."));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case WOOL: {
                if (player.getInventory().contains(Material.IRON_INGOT, 12)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 12));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.WOOL, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory."));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case STAINED_GLASS: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 16)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 16));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.STAINED_GLASS, null, 16, null, (short)((player.getTeam().getName().equalsIgnoreCase("Red"))?14:11)).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case WOOD: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 8));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.WOOD, null, 16).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case OBSIDIAN: {
                if (player.getInventory().contains(Material.EMERALD, 4)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, 4));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.OBSIDIAN, null, 1).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case ENDER_STONE: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 24)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 24));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.ENDER_STONE, null, 8).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Tools
            //Pickaxe
            case IRON_PICKAXE: {
                if (player.getInventory().contains(Material.IRON_INGOT, 12)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 12));
                    this.updateItem(2, 5, new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Pickaxe;;&r&fCost: &b12 &6Gold."));
                    player.getInventory().getItem(pickSlot).setType(Material.IRON_PICKAXE);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_PICKAXE: {
                if (player.getInventory().getItem(pickSlot).getType() != Material.DIAMOND_PICKAXE) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 12)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 12));

                        this.updateItem(2, 5, new GUIItem(Material.DIAMOND_PICKAXE, "&3Pickaxe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Pickaxe Efficiency II;;&r&fCost: &b32 &6Gold."));
                        player.getInventory().getItem(pickSlot).setType(Material.DIAMOND_PICKAXE);
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    if (player.getInventory().getItem(pickSlot).getEnchantmentLevel(Enchantment.DIG_SPEED) < 2) {
                        if (player.getInventory().contains(Material.GOLD_INGOT, 32)) {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                            player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 32));
                            this.updateItem(2, 5, new GUIItem(Material.BARRIER, "&3Pickaxe Upgrade", 1, ";&r&fYou have the max upgrade."));
                            player.getInventory().getItem(pickSlot).addEnchantment(Enchantment.DIG_SPEED, 2);
                        } else {
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                }
                break;
            }

            //Axe
            case STONE_AXE: {
                if (player.getInventory().contains(Material.IRON_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 8));
                    this.updateItem(3, 5, new GUIItem(Material.IRON_AXE, "&3Axe Upgrade", 1, ";&r&fClick here to upgrade to;&7Iron Axe;;&r&fCost: &b8 &6Gold."));
                    player.getInventory().getItem(axeSlot).setType(Material.STONE_AXE);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case IRON_AXE: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 8));
                    this.updateItem(3, 5, new GUIItem(Material.DIAMOND_AXE, "&3Axe Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Axe;;&r&fCost: &b32 &6Gold."));
                    player.getInventory().getItem(axeSlot).setType(Material.IRON_AXE);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_AXE: {
                if (player.getInventory().getItem(axeSlot).getType() != Material.DIAMOND_AXE) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 32)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 32));
                        this.updateItem(3, 5, new GUIItem(Material.BARRIER, "&3Axe Upgrade", 1, ";&r&fYou have the max upgrade."));
                        player.getInventory().getItem(axeSlot).setType(Material.DIAMOND_AXE);
                        player.getInventory().getItem(axeSlot).addEnchantment(Enchantment.DIG_SPEED, 2);
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                }
                break;
            }

            //Shears
            case SHEARS: {
                if (!player.getInventory().contains(Material.SHEARS)) {
                    if (player.getInventory().contains(Material.IRON_INGOT, 16)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                        player.getInventory().addItem(new GUIItem(Material.SHEARS).getItemStack());
                        this.updateItem(4, 5, new GUIItem(Material.BARRIER, "&3Shears", 1, ";&r&fYou already have Shears."));
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.closeInventory();
                    player.sendMessage(TextFormatter.pluginMessage("Player Shop", "You already have Shears."));
                }
                break;
            }

            //Weapons
            //Sword
            case IRON_SWORD: {
                if (player.getInventory().contains(Material.IRON_INGOT, 16)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                    this.updateItem(2, 3, new GUIItem(Material.DIAMOND_SWORD, "&3Sword Upgrade", 1, ";&r&fClick here to upgrade to;&7Diamond Sword;;&r&fCost: &b32 &6Gold."));
                    player.getInventory().getItem(swordSlot).setType(Material.IRON_SWORD);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case DIAMOND_SWORD: {
                if (player.getInventory().getItem(swordSlot).getType() != Material.DIAMOND_SWORD) {
                    if (player.getInventory().contains(Material.GOLD_INGOT, 32)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 32));
                        this.updateItem(2, 3, new GUIItem(Material.BARRIER, "&3Sword Upgrade", 1, ";&r&fYou have the max upgrade."));
                        player.getInventory().getItem(swordSlot).setType(Material.DIAMOND_SWORD);
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                }
                break;
            }

            //Bow and Arrows
            case BOW: {
                if (!player.getInventory().contains(Material.BOW)) {
                    if (player.getInventory().contains(Material.IRON_INGOT, 16)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                        ItemStack stack = new GUIItem(Material.BOW).getItemStack();
                        if (player.getTeam() instanceof CQBlue) {
                            if (((CQBlue) player.getTeam()).getPowerUpgrade() > 0) {
                                stack.addEnchantment(Enchantment.ARROW_DAMAGE, ((CQBlue) player.getTeam()).getPowerUpgrade());
                            }
                        } else {
                            if (((CQRed) player.getTeam()).getPowerUpgrade() > 0) {
                                stack.addEnchantment(Enchantment.ARROW_DAMAGE, ((CQRed) player.getTeam()).getPowerUpgrade());
                            }
                        }
                        player.getInventory().addItem(stack);
                        this.updateItem(3, 3, new GUIItem(Material.BARRIER, "&3Bow", 1, ";&r&fYou already have a Bow."));
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                    player.closeInventory();
                    player.sendMessage(TextFormatter.pluginMessage("Player Shop", "You already have a Bow."));
                }
                break;
            }
            case ARROW: {
                if (player.getInventory().contains(Material.IRON_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 8));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.ARROW, null, 2).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            //Misc
            case WATER_BUCKET: {
                if (player.getInventory().contains(Material.IRON_INGOT, 48)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 48));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.WATER_BUCKET, null, 1).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case GOLDEN_APPLE: {
                if (player.getInventory().contains(Material.GOLD_INGOT, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 8));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.GOLDEN_APPLE, null, 1).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case COOKIE: {
                if (player.getInventory().contains(Material.EMERALD, 2)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, 2));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.COOKIE, "&b&lInstant Cookie", 1).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case FLINT_AND_STEEL: {
                if (player.getInventory().contains(Material.EMERALD, 2)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, 2));
                    ItemStack stack = new ItemStack(Material.FLINT_AND_STEEL, 1, (byte)60);
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(stack);
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case ENDER_PEARL: {
                if (player.getInventory().contains(Material.EMERALD, 8)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, 8));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.ENDER_PEARL, "&3&lEthereal Pearl", 1).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }
            case LADDER: {
                if (player.getInventory().contains(Material.IRON_INGOT, 10)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 0);
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 10));
                    Map<Integer, ItemStack> couldntPlace = player.getInventory().addItem(new GUIItem(Material.LADDER, null, 8).getItemStack());
                    if (couldntPlace.size() > 0) {
                        player.closeInventory();
                        for (Map.Entry<Integer, ItemStack> entry : couldntPlace.entrySet()) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), entry.getValue());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Player Shop", "Some of the items wouldn't fit in your inventory"));
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                }
                break;
            }

            default: {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        }
    }
}

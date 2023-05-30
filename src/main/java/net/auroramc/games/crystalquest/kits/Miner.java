/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Miner extends Kit {

    private final static ItemStack is;

    static {
        is = new ItemStack(Material.DIAMOND_PICKAXE);
        is.addEnchantment(Enchantment.DIG_SPEED, 5);
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
    }

    public Miner() {
        super(0, 1, "&cMiner", "The heart of the team, a Miner will be the ones that every player aims to protect, with the ability to mine ores quicker for everyone.", Material.DIAMOND_PICKAXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        ItemStack stack = new GUIItem(Material.LEATHER_BOOTS).getItemStack();
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getInventory().setBoots(stack);
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItemStack());

        player.getInventory().setItem(1, is);
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(2, axe);
        player.getInventory().setItem(8, CrystalQuest.compass);
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1:
            case 3: {
                return "Receive +3% chance to drop double;&bores when mining.";
            }
            case 4:
            case 5:
            case 2: {
                return "Receive +3% chance to drop double;&bores when mining.;" +
                        "&r - &bReceive +2% chance to drop triple;&bores when mining.";
            }
        }
        return "None";
    }

}

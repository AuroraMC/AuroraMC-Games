/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Archer extends Kit {

    public Archer() {
        super(3, 1, "&cArcher", "A long-time master of archery, punish your enemies from afar with your bow and make them wonder where they're getting attacked from!", Material.BOW, 12000);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItemStack());
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItemStack());
        player.getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(2, axe);
        player.getInventory().setItem(3, new GUIItem(Material.BOW, "&3&lArcher's Bow", 1, ";&r&aLeft-Click to use Quickshot;&r&cFully charge the bow to use Barrage.").getItemStack());

        player.getInventory().setItem(8, CrystalQuest.compass);
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1: {
                return "Increase the maximum amount of Archer Arrows a player can hold by 1 (total of 7);" +
                        "&r - &bReduce the cooldown of Quickshot ability by 1.0 seconds (becomes 44.0 seconds)";
            }
            case 2: {
                return "Reduce the cooldown of Quickshot ability by 1.0 seconds (becomes 43.0 seconds)";
            }
            case 3: {
                return "Increase the maximum amount of Archer Arrows a player can hold by 1 (total of 8);" +
                        "&r - &bReduce the cooldown of Quickshot ability by 1.0 seconds (becomes 42.0 seconds)";
            }
            case 4: {
                return "Reduce the cooldown of Quickshot ability by 1.0 seconds (becomes 41.0 seconds)";
            }
            case 5: {
                return "Increase the maximum amount of Archer Arrows a player can hold by 2 (total of 10);" +
                        "&r - &bReduce the cooldown of Quickshot ability by 1.0 seconds (becomes 40.0 seconds)";
            }
        }
        return "None";
    }
}

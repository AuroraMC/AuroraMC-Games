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

public class Defender extends Kit {

    public Defender() {
        super(1, 1, "&cDefender", "The ability to protect your crystals is one of the most important ones in the game, these players receive blocks that make capturing crystals a nuisance.", Material.STAINED_GLASS, 8000);
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

        player.getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(2, axe);
        player.getInventory().setItem(3, new GUIItem(Material.STAINED_GLASS, null, 12, null, (short)((player.getTeam().getId() == 0)?14:3)).getItemStack());

        player.getInventory().setItem(8, CrystalQuest.compass);
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1: {
                return "Reduce ‘Receive Block’ cooldown by;&b1.0 seconds (becomes 11.0 seconds)";
            }
            case 2: {
                return "Reduce ‘Receive Block’ cooldown by;&b1.0 seconds (becomes 10.0 seconds)";
            }
            case 3: {
                return "Reduce ‘Receive Block’ cooldown by;&b1.0 seconds (becomes 9.0 seconds);" +
                        "&r - &bIncreases the amount of blocks;&byou can receive to 14.";
            }
            case 4: {
                return "Reduce ‘Receive Block’ cooldown by;&b1.0 seconds (becomes 8.0 seconds);" +
                        "&r - &bIncreases the amount of blocks;&byou can receive to 16.";
            }
            case 5: {
                return "Reduce ‘Receive Block’ cooldown by;&b2.0 seconds (becomes 6.0 seconds)";
            }
        }
        return "None";
    }

}

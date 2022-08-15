/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
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
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        ItemStack stack = new GUIItem(Material.LEATHER_BOOTS).getItem();
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getPlayer().getInventory().setBoots(stack);
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItem());

        player.getPlayer().getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItem());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItem();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getPlayer().getInventory().setItem(2, axe);
        player.getPlayer().getInventory().setItem(3, new GUIItem(Material.STAINED_GLASS, null, 12, null, (short)((player.getTeam().getId() == 0)?14:3)).getItem());

        player.getPlayer().getInventory().setItem(8, CrystalQuest.compass);
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
                        "&r - Increases the amount of blocks;&byou can receive to 16.";
            }
            case 5: {
                return "Reduce ‘Receive Block’ cooldown by;&b2.0 seconds (becomes 6.0 seconds)";
            }
        }
        return "None";
    }

}

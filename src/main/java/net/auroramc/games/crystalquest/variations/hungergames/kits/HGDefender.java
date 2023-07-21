/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.hungergames.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.kits.Defender;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class HGDefender extends Defender {

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        ItemStack stack = new GUIItem(Material.LEATHER_BOOTS).getItemStack();
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getInventory().setBoots(stack);
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());


        player.getInventory().setItem(0, new GUIItem(Material.BOW).getItemStack());
        player.getInventory().setItem(4, new GUIItem(Material.ARROW, null, 64).getItemStack());

        player.getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(2, axe);
        player.getInventory().setItem(3, new GUIItem(Material.STAINED_GLASS, null, 12, null, (short)((player.getTeam().getId() == 0)?14:3)).getItemStack());

        player.getInventory().setItem(8, CrystalQuest.compass);
    }

}

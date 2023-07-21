/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.ffa.variations.op.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.ffa.kits.Berserker;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class OPBerserker extends Berserker {

    @Override
    public void onGameStart(AuroraMCServerPlayer auroraMCPlayer) {
        auroraMCPlayer.getInventory().setHelmet(new GUIItem(Material.DIAMOND_HELMET).getItemStack());
        auroraMCPlayer.getInventory().setChestplate(new GUIItem(Material.DIAMOND_CHESTPLATE).getItemStack());
        auroraMCPlayer.getInventory().setLeggings(new GUIItem(Material.DIAMOND_LEGGINGS).getItemStack());
        auroraMCPlayer.getInventory().setBoots(new GUIItem(Material.DIAMOND_BOOTS).getItemStack());

        ItemStack axe = new GUIItem(Material.DIAMOND_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        auroraMCPlayer.getInventory().setItem(0, axe);

        auroraMCPlayer.getInventory().setItem(1, new GUIItem(Material.FISHING_ROD).getItemStack());
        ItemStack bow = new GUIItem(Material.BOW).getItemStack();
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        auroraMCPlayer.getInventory().setItem(2, bow);
        auroraMCPlayer.getInventory().setItem(3, new GUIItem(Material.ARROW, null, 10).getItemStack());
        auroraMCPlayer.getInventory().setItem(4, new GUIItem(Material.GOLDEN_APPLE, null, 64).getItemStack());
        auroraMCPlayer.getInventory().setItem(5, new GUIItem(Material.GOLDEN_APPLE, null, 3, null, (short)1).getItemStack());
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

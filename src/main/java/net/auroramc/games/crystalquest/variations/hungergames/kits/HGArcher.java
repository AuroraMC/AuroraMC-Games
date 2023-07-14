/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.hungergames.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.kits.Archer;
import org.bukkit.Material;

public class HGArcher extends Archer {

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItemStack());
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(0, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        player.getInventory().setItem(1, new GUIItem(Material.WOOD_AXE).getItemStack());
        player.getInventory().setItem(2, new GUIItem(Material.BOW, "&3&lArcher's Bow", 1, ";&r&aLeft-Click to use Quickshot;&r&cFully charge the bow to use Barrage.").getItemStack());

        player.getInventory().setItem(8, CrystalQuest.compass);
    }
}

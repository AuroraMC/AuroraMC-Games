/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.hungergames.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.kits.Economist;
import org.bukkit.Material;

public class HGEconomist extends Economist {

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItemStack());
        player.getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(0, new GUIItem(Material.BOW).getItemStack());
        player.getInventory().setItem(3, new GUIItem(Material.ARROW, null, 64).getItemStack());
        player.getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        player.getInventory().setItem(2, new GUIItem(Material.WOOD_AXE).getItemStack());

        player.getInventory().setItem(8, CrystalQuest.compass);
    }
}

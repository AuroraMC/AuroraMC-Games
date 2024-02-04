/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.ffa.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Berserker extends Kit {


    public Berserker() {
        super(0, 102, "&cBerserker", "Run around and attempt to eliminate other players! Right click with your axe to leap forward.", Material.DIAMOND_AXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer auroraMCPlayer) {
        auroraMCPlayer.getInventory().setHelmet(new GUIItem(Material.IRON_HELMET).getItemStack());
        auroraMCPlayer.getInventory().setChestplate(new GUIItem(Material.IRON_CHESTPLATE).getItemStack());
        auroraMCPlayer.getInventory().setLeggings(new GUIItem(Material.IRON_LEGGINGS).getItemStack());
        auroraMCPlayer.getInventory().setBoots(new GUIItem(Material.IRON_BOOTS).getItemStack());

        auroraMCPlayer.getInventory().setItem(0, new GUIItem(Material.DIAMOND_AXE).getItemStack());
        auroraMCPlayer.getInventory().setItem(1, new GUIItem(Material.FISHING_ROD).getItemStack());
        auroraMCPlayer.getInventory().setItem(2, new GUIItem(Material.BOW).getItemStack());
        auroraMCPlayer.getInventory().setItem(3, new GUIItem(Material.ARROW, null, 10).getItemStack());
        auroraMCPlayer.getInventory().setItem(4, new GUIItem(Material.GOLDEN_APPLE, null, 3).getItemStack());
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

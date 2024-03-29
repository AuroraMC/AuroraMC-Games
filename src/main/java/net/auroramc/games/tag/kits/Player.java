/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Player extends Kit {


    public Player() {
        super(0, 106, "&cPlayer", "Tag! You're it!", Material.IRON_AXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new GUIItem(Material.IRON_AXE).getItemStack());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItemStack());
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;

public class Ecologist extends Kit {

    public Ecologist() {
        super(4, 1, "&3&lEcologist", "Some healer kit description", Material.EMERALD, 15000);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        player.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItem());
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItem());
        player.getPlayer().getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItem());
        player.getPlayer().getInventory().setItem(2, new GUIItem(Material.WOOD_AXE).getItem());

        player.getPlayer().getInventory().setItem(8, CrystalQuest.compass);
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {

        }
        return "None";
    }
}

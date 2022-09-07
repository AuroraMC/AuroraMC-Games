/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;

public class Archer extends Kit {

    public Archer() {
        super(3, 1, "&3&lArcher", "A long-time master of archery, punish your enemies from afar with your bow and make them wonder where they're getting attacked from!", Material.BOW, 12000);
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
        player.getPlayer().getInventory().setItem(2, new GUIItem(Material.BOW).getItem());

        player.getPlayer().getInventory().setItem(8, CrystalQuest.compass);
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
        return "None";    }
}

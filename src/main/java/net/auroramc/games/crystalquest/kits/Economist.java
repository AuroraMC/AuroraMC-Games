/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;

public class Economist extends Kit {

    public Economist() {
        super(4, 1, "&cEconomist", "Intrigued about the mining robots, economists spent hours finding out how to duplicate that technology to generate resources themselves.", Material.EMERALD, 20000);
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
            case 1: {
                return "Reduce each cooldown by 0.5 seconds (Iron - 19.5 seconds, Gold - 39.5 seconds, Emerald 59.5 seconds)";
            }
            case 2: {
                return "Reduce each cooldown by 0.5 seconds (Iron - 19 seconds, Gold - 39 seconds, Emerald 59 seconds;)" +
                        "&r - &bReceive +1 Iron per ability (Total of 4)";
            }
            case 3: {
                return "Reduce each cooldown by 0.5 seconds (Iron - 18.5 seconds, Gold - 38.5 seconds, Emerald 58.5 seconds)";
            }
            case 4: {
                return "Reduce each cooldown by 0.5 seconds (Iron - 18 seconds, Gold - 38 seconds, Emerald 58 seconds);" +
                        "&r - &bReceive +1 Gold per ability (Total of 3)";
            }
            case 5: {
                return "Reduce each cooldown by 0.5 seconds (Iron - 17.5 seconds, Gold - 37.5 seconds, Emerald 57.5 seconds)";
            }
        }
        return "None";
    }
}

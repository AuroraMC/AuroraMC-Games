/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Blinker extends Kit {

    public Blinker() {
        super(1, 106, "&cBlinker", "A master of teleportation, these players can teleport to a random spawn point in the map. Blink and you'll be left wondering exactly where they've gone...", Material.ENDER_PEARL, 12500);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.IRON_AXE).getItem());
        player.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItem());
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

        player.getPlayer().getInventory().setItem(8, new GUIItem(Material.ENDER_PEARL).getItem());

    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1: {
                return "Reduce Ability Use Cooldown by 1 seconds (Becomes 39.0 seconds)";
            }
            case 2: {
                return "Reduce Ability Use Cooldown by 2 seconds (Becomes 37.0 seconds)";
            }
            case 3: {
                return "Reduce Ability Use Cooldown by 1 seconds (Becomes 36.0 seconds)";
            }
            case 4: {
                return "Reduce Ability Use Cooldown by 1 seconds (Becomes 35.0 seconds)";
            }
            case 5: {
                return "Reduce Ability Use Cooldown by 2.5 seconds (Becomes 32.5.0 seconds)";
            }
        }
        return "None";
    }
}

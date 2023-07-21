/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Blinker extends Kit {

    public Blinker() {
        super(1, 106, "&cBlinker", "A master of teleportation, these players can teleport to a random spawn point in the map. Blink and you'll be left wondering exactly where they've gone...", Material.ENDER_PEARL, 12500);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new GUIItem(Material.IRON_AXE).getItemStack());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItemStack());
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(8, new GUIItem(Material.ENDER_PEARL, "&3&lBlink", 1, ";&aRight-click to use blink!").getItemStack());

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

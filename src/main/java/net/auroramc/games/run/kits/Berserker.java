/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Berserker extends Kit {


    public Berserker() {
        super(0, 104, "&cBerserker", "Run around as blocks fall below your feet, right click with your axe to leap into the air when falling!", Material.LEATHER_BOOTS, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().clear();
        ItemStack stack = new GUIItem(Material.DIAMOND_AXE).getItemStack();
        player.getInventory().setItem(0, stack);
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

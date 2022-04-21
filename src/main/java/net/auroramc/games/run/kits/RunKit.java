/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.run.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RunKit extends Kit {


    public RunKit() {
        super(0, 104, "&cRun Kit", "A kit for Run. Simple.", Material.LEATHER_BOOTS, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        ItemStack stack = new GUIItem(Material.DIAMOND_AXE).getItem();
        player.getPlayer().getInventory().setItem(0, stack);
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

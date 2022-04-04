/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class FFAKit extends Kit {


    public FFAKit() {
        super(0, 102, "&3&lBrawler", "A kit description", Material.DIAMOND_AXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer auroraMCPlayer) {
        auroraMCPlayer.getPlayer().getInventory().setHelmet(new GUIItem(Material.IRON_HELMET).getItem());
        auroraMCPlayer.getPlayer().getInventory().setChestplate(new GUIItem(Material.IRON_CHESTPLATE).getItem());
        auroraMCPlayer.getPlayer().getInventory().setLeggings(new GUIItem(Material.IRON_LEGGINGS).getItem());
        auroraMCPlayer.getPlayer().getInventory().setBoots(new GUIItem(Material.IRON_BOOTS).getItem());

        auroraMCPlayer.getPlayer().getInventory().setItem(0, new GUIItem(Material.DIAMOND_AXE).getItem());
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

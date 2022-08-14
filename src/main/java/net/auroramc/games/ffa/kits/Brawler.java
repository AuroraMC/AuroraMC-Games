/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Brawler extends Kit {


    public Brawler() {
        super(0, 102, "&3&lBerserker", "Run around and attempt to eliminate other players! Right click with your axe to leap forward.", Material.DIAMOND_AXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer auroraMCPlayer) {
        auroraMCPlayer.getPlayer().getInventory().setHelmet(new GUIItem(Material.IRON_HELMET).getItem());
        auroraMCPlayer.getPlayer().getInventory().setChestplate(new GUIItem(Material.IRON_CHESTPLATE).getItem());
        auroraMCPlayer.getPlayer().getInventory().setLeggings(new GUIItem(Material.IRON_LEGGINGS).getItem());
        auroraMCPlayer.getPlayer().getInventory().setBoots(new GUIItem(Material.IRON_BOOTS).getItem());

        auroraMCPlayer.getPlayer().getInventory().setItem(0, new GUIItem(Material.DIAMOND_AXE).getItem());
        auroraMCPlayer.getPlayer().getInventory().setItem(1, new GUIItem(Material.FISHING_ROD).getItem());
        auroraMCPlayer.getPlayer().getInventory().setItem(2, new GUIItem(Material.BOW).getItem());
        auroraMCPlayer.getPlayer().getInventory().setItem(3, new GUIItem(Material.ARROW, null, 10).getItem());
        auroraMCPlayer.getPlayer().getInventory().setItem(4, new GUIItem(Material.GOLDEN_APPLE, null, 3).getItem());
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

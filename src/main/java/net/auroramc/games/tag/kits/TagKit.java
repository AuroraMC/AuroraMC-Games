/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class TagKit extends Kit {


    public TagKit() {
        super(0, 106, "&cTag Kit", "A kit for Tag. Simple.", Material.IRON_AXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.IRON_AXE).getItem());
        player.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItem());
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

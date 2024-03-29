/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class Potato extends Kit {


    public Potato() {
        super(0, 101, "&cPotato", "Potatoes are cool, right? Well, in this case they're not! Avoid having a potato in your inventory at all costs.", Material.POTATO_ITEM, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

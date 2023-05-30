/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

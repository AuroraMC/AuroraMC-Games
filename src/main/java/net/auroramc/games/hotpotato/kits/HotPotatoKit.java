/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class HotPotatoKit extends Kit {


    public HotPotatoKit() {
        super(0, 101, "&cHotPotato Kit", "A kit for HotPOtato. Simple.", Material.POTATO_ITEM, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

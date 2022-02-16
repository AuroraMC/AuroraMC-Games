/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class AnotherSpleefKit extends Kit {


    public AnotherSpleefKit() {
        super(1, 2, "&cAnother Spleef Kit", "Another kit for Spleef.", Material.SNOW_BLOCK, 100);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.IRON_SPADE, "&3&lBrandon Stinks").getItem());

    }
}

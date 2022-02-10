/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class MoreSpleefKit extends Kit {

    public MoreSpleefKit() {
        super(0, 3, "&cMore Spleef Kit", "A better kit for Spleef.", Material.SNOW_BALL, 1000);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.DIAMOND_SPADE, "&3&lBlock is very very very bad.").getItem());
    }
}

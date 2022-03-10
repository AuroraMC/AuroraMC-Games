/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;

public class SpleefKit extends Kit {


    public SpleefKit() {
        super(0, 2, "&cSpleef Kit", "A kit for Spleef.", Material.SNOW_BALL, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.IRON_SPADE, "&3&lSpade of doom").getItem());
    }
}

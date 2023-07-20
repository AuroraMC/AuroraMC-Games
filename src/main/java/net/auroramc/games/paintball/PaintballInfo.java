/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.paintball;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.paintball.variations.infiniteammo.InfiniteAmmoInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class PaintballInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("INFINITE_AMMO", new InfiniteAmmoInfo());
    }

    public PaintballInfo() {
        super(3, "Paintball", Paintball.class, "Throw your paintballs at the enemy team to steal an enemy life.\n" +
                "Use gold to buy upgrades. +2 gold per kill.\n" +
                "Steal all of your enemies lifes to win.", "PAINTBALL", false, variations, Material.SNOW_BALL, (short)0);
    }
}

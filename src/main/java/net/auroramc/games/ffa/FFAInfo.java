/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.ffa;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.ffa.variations.knockback.KnockbackInfo;
import net.auroramc.games.ffa.variations.op.OPInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class FFAInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("OVERPOWERED", new OPInfo());
        variations.put("KNOCKBACK", new KnockbackInfo());
    }

    public FFAInfo() {
        super(102, "FFA", FFA.class, "Kill your opponents and be the last one alive. That's it. Yes, it's really that simple.", "FFA", false, variations, Material.IRON_AXE, (short)0);
    }
}

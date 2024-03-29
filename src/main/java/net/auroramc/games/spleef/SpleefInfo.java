/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.spleef.variations.blind.BlindInfo;
import net.auroramc.games.spleef.variations.sandstorm.SandstormInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class SpleefInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("SANDSTORM", new SandstormInfo());
        variations.put("BLIND", new BlindInfo());
    }

    public SpleefInfo() {
        super(100, "Spleef", Spleef.class, "Destroy blocks underneath your opponents!\nThrow snowballs to knock players off\nBe the last player alive!", "SPLEEF", false, variations, Material.IRON_SPADE , (short)0);
    }
}

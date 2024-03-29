/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.tag.variations.hacker.HackerSimulatorInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class TagInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("HACKER_SIMULATOR", new HackerSimulatorInfo());
    }

    public TagInfo() {
        super(103, "Tag", Tag.class, "Avoid getting tagged and be the last person alive!", "TAG", false, variations, Material.LEASH, (short)0);
    }
}

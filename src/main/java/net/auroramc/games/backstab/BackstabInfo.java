/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.backstab;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class BackstabInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();

    }

    public BackstabInfo() {
        super(2, "Backstab", Backstab.class, "Maneuver your way through enemy territory and collect their crystals " +
                "to allow you to eliminate them. Upgrade armour and enchants to give your team an advantage, but remember: §bProtect your crystals at all time!", "CRYSTAL_QUEST", false, variations, Material.IRON_SWORD, (short)0);
    }
}

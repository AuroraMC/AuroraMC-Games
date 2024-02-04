/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.oresbegone;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class OresBeGoneInfo extends GameVariationInfo {
    public OresBeGoneInfo() {
        super(3, "Ores-Be-Gone", OresBeGone.class, "It's not Johnny B Goode, it's ores be gone.", null, Material.BEDROCK, (short)0);
    }
}

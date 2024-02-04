/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.variations.blind;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class BlindInfo extends GameVariationInfo {
    public BlindInfo() {
        super(1, "Blind", Blind.class, "Oh no, I can't see! Well, now you can't either!", null, Material.PUMPKIN, (short)0);
    }
}

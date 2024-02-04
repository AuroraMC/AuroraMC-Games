/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.variations.sandstorm;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class SandstormInfo extends GameVariationInfo {
    public SandstormInfo() {
        super(0, "Sandstorm", Sandstorm.class, "Cause we know you all love Darude - Sandstorm.", null, Material.SAND, (short)0);
    }
}

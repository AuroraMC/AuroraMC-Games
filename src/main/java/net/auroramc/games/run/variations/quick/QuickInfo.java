/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.variations.quick;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class QuickInfo extends GameVariationInfo {
    public QuickInfo() {
        super(0, "Quick", Quick.class, "Welcome to Quick Run! You might want to hold shift, just saying...", null, Material.RABBIT_FOOT, (short)0);
    }
}

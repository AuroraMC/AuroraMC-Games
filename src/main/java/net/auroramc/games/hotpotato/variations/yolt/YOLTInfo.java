/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.variations.yolt;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class YOLTInfo extends GameVariationInfo {
    public YOLTInfo() {
        super(0, "YOLT", YOLT.class, "You only live twice. Or at least I think that's how it goes?", null, Material.RED_MUSHROOM, (short)0);
    }
}

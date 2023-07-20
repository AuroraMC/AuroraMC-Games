/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato.variations.yolt;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class YOLTInfo extends GameVariationInfo {
    public YOLTInfo() {
        super(0, "YOLT", YOLT.class, "You only live twice. Or at least I think that's how it goes?", null, Material.RED_MUSHROOM, (short)0);
    }
}

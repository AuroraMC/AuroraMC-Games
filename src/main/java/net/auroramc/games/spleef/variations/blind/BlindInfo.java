/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.spleef.variations.blind;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class BlindInfo extends GameVariationInfo {
    public BlindInfo() {
        super(1, "Blind", Blind.class, "Oh no, I can't see! Well, now you can't either!", null, Material.PUMPKIN, (short)0);
    }
}

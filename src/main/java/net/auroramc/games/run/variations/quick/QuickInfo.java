/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.run.variations.quick;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class QuickInfo extends GameVariationInfo {
    public QuickInfo() {
        super(0, "Quick", Quick.class, "Welcome to Quick Run! You might want to hold shift, just saying...", null, Material.RABBIT_FOOT, (short)0);
    }
}

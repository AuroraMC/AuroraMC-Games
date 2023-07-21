/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag.variations.hacker;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class HackerSimulatorInfo extends GameVariationInfo {
    public HackerSimulatorInfo() {
        super(1, "Hacker Simulator", HackerSimulator.class, "Pretend to be a hacker. But don't actually hack. That would be bad.", null, Material.RABBIT_FOOT, (short)0);
    }
}

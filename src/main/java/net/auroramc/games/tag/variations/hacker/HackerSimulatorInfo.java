/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag.variations.hacker;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class HackerSimulatorInfo extends GameVariationInfo {
    public HackerSimulatorInfo() {
        super(1, "Hacker Simulator", HackerSimulator.class, "Pretend to be a hacker. But don't actually hack. That would be bad.", null, Material.RABBIT_FOOT, (short)0);
    }
}

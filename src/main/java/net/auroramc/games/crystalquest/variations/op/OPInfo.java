/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.op;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class OPInfo extends GameVariationInfo {
    public OPInfo() {
        super(0, "Overpowered", OP.class, "It's overpowered. Simple.", null, Material.DIAMOND_CHESTPLATE, (short)0);
    }
}

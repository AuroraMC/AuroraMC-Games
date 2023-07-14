/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.op;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.games.GameVariationInfo;

public class OPInfo extends GameVariationInfo {
    public OPInfo() {
        super(0, "Overpowered", OP.class, "It's overpowered. Simple.", null);
    }
}

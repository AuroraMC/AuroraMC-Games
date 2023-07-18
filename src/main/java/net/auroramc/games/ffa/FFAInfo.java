/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.ffa;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.ffa.variations.knockback.KnockbackInfo;
import net.auroramc.games.ffa.variations.op.OPInfo;

import java.util.HashMap;
import java.util.Map;

public class FFAInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("OVERPOWERED", new OPInfo());
        variations.put("KNOCKBACK", new KnockbackInfo());
    }

    public FFAInfo() {
        super(102, "FFA", FFA.class, "Kill your opponents and be the last one alive. That's it. Yes, it's really that simple.", "FFA", false, variations);
    }
}

/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.run;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.run.variations.blind.BlindInfo;
import net.auroramc.games.run.variations.quick.QuickInfo;
import net.auroramc.games.spleef.variations.sandstorm.SandstormInfo;

import java.util.HashMap;
import java.util.Map;

public class RunInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("QUICK", new QuickInfo());
        variations.put("BLIND", new BlindInfo());
    }

    public RunInfo() {
        super(104, "Run", Run.class, "Run and don't stop.\nBe the last one alive.", "RUN", false, variations);
    }
}

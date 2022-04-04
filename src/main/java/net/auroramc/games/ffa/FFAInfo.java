/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa;

import net.auroramc.engine.api.games.GameInfo;

public class FFAInfo extends GameInfo {

    public FFAInfo() {
        super(102, "FFA", FFA.class, "A game description", "FFA", false, "v0.0.1-ALPHA");
    }
}

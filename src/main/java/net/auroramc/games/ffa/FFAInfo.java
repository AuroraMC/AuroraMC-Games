/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.ffa;

import net.auroramc.engine.api.games.GameInfo;

public class FFAInfo extends GameInfo {

    public FFAInfo() {
        super(102, "FFA", FFA.class, "Kill your opponents and be the last one alive. That's it. Yes, it's really that simple.", "FFA", false, "v0.0.1-ALPHA");
    }
}

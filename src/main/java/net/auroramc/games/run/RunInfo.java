/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.run;

import net.auroramc.engine.api.games.GameInfo;

public class RunInfo extends GameInfo {

    public RunInfo() {
        super(104, "Run", Run.class, "Run and don't stop.\nBe the last one alive.", "RUN", false, "v0.0.1-ALPHA");
    }
}

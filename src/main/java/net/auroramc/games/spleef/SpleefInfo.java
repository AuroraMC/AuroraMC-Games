/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.spleef;

import net.auroramc.engine.api.games.GameInfo;

public class SpleefInfo extends GameInfo {

    public SpleefInfo() {
        super(100, "Spleef", Spleef.class, "Destroy blocks underneath your opponents!\nThrow snowballs to knock players off\nBe the last player alive!", "SPLEEF", false, "v0.0.1-ALPHA");
    }
}

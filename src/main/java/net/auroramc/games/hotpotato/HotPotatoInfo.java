/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato;

import net.auroramc.engine.api.games.GameInfo;

public class HotPotatoInfo extends GameInfo {

    public HotPotatoInfo() {
        super(101, "Hot Potato", HotPotato.class, "Avoid the having the Hot Potato when it explodes!.\nPunch players to pass on the Hot Potato.\nBe the last alive!", "HOT_POTATO", false, "v0.0.1-ALPHA");
    }
}

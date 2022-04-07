/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato;

import net.auroramc.engine.api.games.GameInfo;

public class HotPotatoInfo extends GameInfo {

    public HotPotatoInfo() {
        super(101, "Hot Potato", HotPotato.class, "A game description", "HOT_POTATO", false, "v0.0.1-ALPHA");
    }
}

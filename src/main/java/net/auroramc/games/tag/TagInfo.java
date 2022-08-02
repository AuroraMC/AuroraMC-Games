/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag;

import net.auroramc.engine.api.games.GameInfo;

public class TagInfo extends GameInfo {

    public TagInfo() {
        super(103, "Tag", Tag.class, "Avoid getting tagged and be the last person alive!", "TAG", false, "v0.0.1-ALPHA");
    }
}

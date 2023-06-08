/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag;

import net.auroramc.engine.api.games.GameInfo;

public class TagInfo extends GameInfo {

    public TagInfo() {
        super(103, "Tag", Tag.class, "Avoid getting tagged and be the last person alive!", "TAG", false, "v0.0.1-ALPHA");
    }
}

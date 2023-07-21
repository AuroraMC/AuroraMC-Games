/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag.variations;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.games.tag.Tag;

public abstract class TagVariation extends GameVariation<Tag> {
    public TagVariation(Tag game) {
        super(game);
    }
}

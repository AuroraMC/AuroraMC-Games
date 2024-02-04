/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag.variations;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.games.tag.Tag;

public abstract class TagVariation extends GameVariation<Tag> {
    public TagVariation(Tag game) {
        super(game);
    }
}

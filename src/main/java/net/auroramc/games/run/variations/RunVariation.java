/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.variations;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.run.Run;

public abstract class RunVariation extends GameVariation<Run> {
    public RunVariation(Run game) {
        super(game);
    }
}

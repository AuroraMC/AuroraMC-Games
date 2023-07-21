/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

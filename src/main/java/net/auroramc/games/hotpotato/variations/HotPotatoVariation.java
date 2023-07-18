/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato.variations;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.hotpotato.entities.Potato;

public abstract class HotPotatoVariation extends GameVariation<HotPotato> {
    public HotPotatoVariation(HotPotato game) {
        super(game);
    }

    public abstract int onGeneratePotatoes(int potatoes, int playersAlive);

    public abstract boolean onExplode(Potato potato);
}

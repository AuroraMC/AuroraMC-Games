/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
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

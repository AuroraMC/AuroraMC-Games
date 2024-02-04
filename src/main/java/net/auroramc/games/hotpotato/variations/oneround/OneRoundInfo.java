/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.variations.oneround;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class OneRoundInfo extends GameVariationInfo {
    public OneRoundInfo() {
        super(1, "One Round", OneRound.class, "Want a quicker game of Hot Potato? Fine, hows a one round game?", null, Material.TNT, (short)0);
    }
}

/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato.variations.oneround;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class OneRoundInfo extends GameVariationInfo {
    public OneRoundInfo() {
        super(1, "One Round", OneRound.class, "Want a quicker game of Hot Potato? Fine, hows a one round game?", null, Material.TNT, (short)0);
    }
}

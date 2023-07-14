/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.hungergames;

import net.auroramc.engine.api.games.GameVariationInfo;

public class HungerGamesInfo extends GameVariationInfo {
    public HungerGamesInfo() {
        super(4, "Hunger Games", HungerGames.class, "Just like Katniss Everdeen, you may only use a bow.", null);
    }
}

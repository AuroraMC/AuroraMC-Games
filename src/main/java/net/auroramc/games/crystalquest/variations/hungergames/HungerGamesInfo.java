/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.hungergames;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class HungerGamesInfo extends GameVariationInfo {
    public HungerGamesInfo() {
        super(4, "Hunger Games", HungerGames.class, "Just like Katniss Everdeen, you may only use a bow.", null, Material.BOW, (short)0);
    }
}

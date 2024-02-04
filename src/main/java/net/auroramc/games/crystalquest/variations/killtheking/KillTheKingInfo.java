/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.killtheking;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class KillTheKingInfo extends GameVariationInfo {
    public KillTheKingInfo() {
        super(5, "Kill the King", KillTheKing.class, "Scar: Long live THE KING!", null, Material.GOLD_HELMET, (short) 0);
    }
}

/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.staffvscommunity;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class StaffVsCommunityInfo extends GameVariationInfo {
    public StaffVsCommunityInfo() {
        super(6, "Staff VS Community", StaffVsCommunity.class, "Exactly what is sounds like. All staff members vs all community members.", null, Material.ENDER_STONE, (short)0);
    }
}

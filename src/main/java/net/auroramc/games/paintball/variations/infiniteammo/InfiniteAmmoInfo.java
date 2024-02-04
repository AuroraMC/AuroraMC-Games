/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.variations.infiniteammo;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class InfiniteAmmoInfo extends GameVariationInfo {
    public InfiniteAmmoInfo() {
        super(0, "Infinite Ammo", InfiniteAmmo.class, "Well, at least if you have a bad shot you wont run out of ammo.", null, Material.SNOW_BLOCK, (short)0);
    }
}

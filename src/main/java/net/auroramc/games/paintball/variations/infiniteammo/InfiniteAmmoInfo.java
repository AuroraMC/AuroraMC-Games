/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.paintball.variations.infiniteammo;

import net.auroramc.engine.api.games.GameVariationInfo;

public class InfiniteAmmoInfo extends GameVariationInfo {
    public InfiniteAmmoInfo() {
        super(0, "Infinite Ammo", InfiniteAmmo.class, "Well, at least if you have a bad shot you wont run out of ammo.", null);
    }
}

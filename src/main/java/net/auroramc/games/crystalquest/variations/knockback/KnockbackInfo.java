/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.knockback;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class KnockbackInfo extends GameVariationInfo {
    public KnockbackInfo() {
        super(1, "Knockback", Knockback.class, "Fly, Fly my pretties!", null, Material.COOKIE, (short)0);
    }
}

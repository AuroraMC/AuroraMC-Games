/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.knockback;

import net.auroramc.engine.api.games.GameVariationInfo;

public class KnockbackInfo extends GameVariationInfo {
    public KnockbackInfo() {
        super(1, "Knockback", Knockback.class, "Fly, Fly my pretties!", null);
    }
}

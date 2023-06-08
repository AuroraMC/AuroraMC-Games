/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.backstab;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.games.crystalquest.CrystalQuest;

public class BackstabInfo extends GameInfo {

    public BackstabInfo() {
        super(2, "Backstab", Backstab.class, "Maneuver your way through enemy territory and collect their crystals " +
                "to allow you to eliminate them. Upgrade armour and enchants to give your team an advantage, but remember: §bProtect your crystals at all time!", "CRYSTAL_QUEST", false, "v0.1.3-ALPHA");
    }
}

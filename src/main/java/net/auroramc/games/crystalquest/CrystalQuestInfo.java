/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.engine.api.games.GameInfo;

public class CrystalQuestInfo extends GameInfo {

    public CrystalQuestInfo() {
        super(1, "Crystal Quest", CrystalQuest.class, "Protect your crystals and steal enemy crystals.\n" +
                "Mine ores or collect resources from your mining robot.\n" +
                "Upgrade your gear and be the last alive.", "CRYSTAL_QUEST", false, "v0.1.3-ALPHA");
    }
}

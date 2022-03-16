/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.engine.api.games.GameInfo;

public class CrystalQuestInfo extends GameInfo {

    public CrystalQuestInfo() {
        super(1, "Crystal Quest", CrystalQuest.class, "Maneuver your way through enemy territory and collect their crystals" +
                "to allow you to eliminate them. Upgrade armour and enchants to give your team an advantage, but remember: **Protect your crystals at all time!**", "CRYSTAL_QUEST", false);
    }
}

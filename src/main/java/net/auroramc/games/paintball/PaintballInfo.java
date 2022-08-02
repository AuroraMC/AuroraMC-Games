/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball;

import net.auroramc.engine.api.games.GameInfo;

public class PaintballInfo extends GameInfo {

    public PaintballInfo() {
        super(3, "Paintball", Paintball.class, "Throw your paintballs at the enemy team to steal an enemy life.\n" +
                "Use gold to buy upgrades. +2 gold per kill.\n" +
                "Steal all of your enemies lifes to win.", "PAINTBALL", false, "v0.0.1-ALPHA");
    }
}

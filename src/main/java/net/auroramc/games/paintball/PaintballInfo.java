/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball;

import net.auroramc.engine.api.games.GameInfo;

public class PaintballInfo extends GameInfo {

    public PaintballInfo() {
        super(3, "Paintball", Paintball.class, "A game description", "PAINTBALL", false, "v0.0.1-ALPHA");
    }
}

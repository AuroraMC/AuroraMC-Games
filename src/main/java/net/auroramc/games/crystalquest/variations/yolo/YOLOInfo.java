/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.yolo;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class YOLOInfo extends GameVariationInfo {
    public YOLOInfo() {
        super(2, "YOLO", YOLO.class, "Cause ya know, you only live once. Or maybe a few times. But not many.", null, Material.RED_MUSHROOM, (short)0);
    }
}

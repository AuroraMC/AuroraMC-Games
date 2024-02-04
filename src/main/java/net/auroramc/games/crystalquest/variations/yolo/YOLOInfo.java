/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.yolo;

import net.auroramc.engine.api.games.GameVariationInfo;
import org.bukkit.Material;

public class YOLOInfo extends GameVariationInfo {
    public YOLOInfo() {
        super(2, "YOLO", YOLO.class, "Cause ya know, you only live once. Or maybe a few times. But not many.", null, Material.RED_MUSHROOM, (short)0);
    }
}

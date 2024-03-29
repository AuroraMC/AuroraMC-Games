/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.hotpotato;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.hotpotato.variations.oneround.OneRoundInfo;
import net.auroramc.games.hotpotato.variations.yolt.YOLTInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class HotPotatoInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("ONE_ROUND", new OneRoundInfo());
        variations.put("YOLT", new YOLTInfo());
    }

    public HotPotatoInfo() {
        super(101, "Hot Potato", HotPotato.class, "Avoid the having the Hot Potato when it explodes!.\nPunch players to pass on the Hot Potato.\nBe the last alive!", "HOT_POTATO", false, variations, Material.BAKED_POTATO, (short)0);
    }
}

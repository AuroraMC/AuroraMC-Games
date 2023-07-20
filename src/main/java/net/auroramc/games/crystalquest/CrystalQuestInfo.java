/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.engine.api.games.GameInfo;
import net.auroramc.engine.api.games.GameVariationInfo;
import net.auroramc.games.crystalquest.variations.hungergames.HungerGamesInfo;
import net.auroramc.games.crystalquest.variations.killtheking.KillTheKingInfo;
import net.auroramc.games.crystalquest.variations.knockback.KnockbackInfo;
import net.auroramc.games.crystalquest.variations.op.OPInfo;
import net.auroramc.games.crystalquest.variations.oresbegone.OresBeGoneInfo;
import net.auroramc.games.crystalquest.variations.staffvscommunity.StaffVsCommunityInfo;
import net.auroramc.games.crystalquest.variations.yolo.YOLOInfo;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class CrystalQuestInfo extends GameInfo {

    private static final Map<String, GameVariationInfo> variations;

    static {
        variations = new HashMap<>();
        variations.put("OP", new OPInfo());
        variations.put("YOLO", new YOLOInfo());
        variations.put("STAFF_VS_COMMUNITY", new StaffVsCommunityInfo());
        variations.put("HUNGER_GAMES", new HungerGamesInfo());
        variations.put("KILL_THE_KING", new KillTheKingInfo());
        variations.put("KNOCKBACK", new KnockbackInfo());
        variations.put("ORES_BE_GONE", new OresBeGoneInfo());
    }

    public CrystalQuestInfo() {
        super(1, "Crystal Quest", CrystalQuest.class, "Protect your crystals and steal enemy crystals.\n" +
                "Mine ores or collect resources from your mining robot.\n" +
                "Upgrade your gear and be the last alive.", "CRYSTAL_QUEST", false, variations, Material.NETHER_STAR, (short)0);
    }
}

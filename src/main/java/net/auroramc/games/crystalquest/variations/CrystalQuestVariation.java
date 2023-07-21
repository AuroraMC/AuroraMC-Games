/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations;

import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import org.bukkit.Material;

public abstract class CrystalQuestVariation extends GameVariation<CrystalQuest> {
    public CrystalQuestVariation(CrystalQuest game) {
        super(game);
    }

    public abstract void onCrystalDestroy();

    public abstract void onMineGenerate();

    public abstract void onCrystalCaptured(Crystal crystal);

    public abstract void onCrystalDead(Crystal crystal);

    public abstract void onCrystalReturned(Crystal crystal);

    public abstract boolean onShopPurchase(Material material);

    public abstract GUIItem onShopDisplayItem(GUIItem material);
}

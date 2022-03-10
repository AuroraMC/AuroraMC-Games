/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TeamShop extends GUI {

    private AuroraMCGamePlayer player;

    public TeamShop(AuroraMCGamePlayer player) {
        super("&3&lTeam Shop", 5, true);
        fill("&3&lTeam Shop", null);



        this.player = player;
    }

    @Override
    public void onClick(int row, int column, ItemStack itemStack, ClickType clickType) {

    }
}

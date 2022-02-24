/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PlayerShop extends GUI {

    private AuroraMCGamePlayer player;

    public PlayerShop(AuroraMCGamePlayer player) {
        super("&3&lPlayer Shop", 5, true);

        this.player = player;
    }

    @Override
    public void onClick(int row, int column, ItemStack itemStack, ClickType clickType) {

    }
}

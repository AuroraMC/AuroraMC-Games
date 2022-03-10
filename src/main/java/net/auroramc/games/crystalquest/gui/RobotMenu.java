/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.gui;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.games.crystalquest.entities.MiningRobot;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class RobotMenu extends GUI {


    public RobotMenu(AuroraMCPlayer player, MiningRobot robot) {
        super(robot.getEntity().getCustomName() + " (Level " + robot.getLevel() + ")", 2, true);
        border(robot.getEntity().getCustomName() + " (Level " + robot.getLevel() + ")",null);
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}

/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.events.entity.FoodLevelChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HungerListener implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getLevel() < 30) {
            e.setLevel(30);
        }
    }

}

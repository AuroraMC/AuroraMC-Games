/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getFoodLevel() < 30) {
            e.setFoodLevel(30);
        }
    }

}

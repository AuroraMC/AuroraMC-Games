/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
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

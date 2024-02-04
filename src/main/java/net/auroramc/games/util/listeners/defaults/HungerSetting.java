/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.entity.FoodLevelChangeEvent;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class HungerSetting implements Listener {

    private final static HungerSetting instance;

    static {
        instance = new HungerSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (EngineAPI.getActiveGame().getHunger() > -1) {
            e.setLevel(EngineAPI.getActiveGame().getHunger());
        }
    }


    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        FoodLevelChangeEvent.getHandlerList().unregister(instance);
    }
}

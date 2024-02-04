/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.core.api.events.entity.FoodLevelChangeEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisableHungerListener implements Listener {

    private final static DisableHungerListener instance;

    static {
        instance = new DisableHungerListener();
    }

    @EventHandler
    public void onMove(FoodLevelChangeEvent e) {
        if (EngineAPI.getActiveGame().getHunger() > -1) {
            e.setLevel(EngineAPI.getActiveGame().getHunger());
        } else if (e.getLevel() < 30) {
            e.setLevel(30);
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        FoodLevelChangeEvent.getHandlerList().unregister(instance);
    }
}

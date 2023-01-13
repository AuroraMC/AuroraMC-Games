/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.settings;

import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class DisableWeatherListener implements Listener {

    private final static DisableWeatherListener instance;

    static {
        instance = new DisableWeatherListener();
    }

    @EventHandler
    public void onMove(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        WeatherChangeEvent.getHandlerList().unregister(instance);
    }
}

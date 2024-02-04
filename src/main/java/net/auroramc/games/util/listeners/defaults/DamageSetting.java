/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.entity.EntityDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageByEntityEvent;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DamageSetting implements Listener {

    private final static DamageSetting instance;

    static {
        instance = new DamageSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(PlayerDamageEvent e) {
        if (!EngineAPI.getActiveGame().isDamageAll()) {
            e.setCancelled(true);
            return;
        }
        if (e instanceof PlayerDamageByPlayerEvent && !EngineAPI.getActiveGame().isDamagePvP()) {
            e.setCancelled(true);
            return;
        }
        if (e instanceof EntityDamageByPlayerEvent && !EngineAPI.getActiveGame().isDamagePvE()) {
            e.setCancelled(true);
            return;
        }
        if (e instanceof PlayerDamageByEntityEvent && !EngineAPI.getActiveGame().isDamageEvP()) {
            e.setCancelled(true);
            return;
        }
        if (e.getCause() == PlayerDamageEvent.DamageCause.FALL && !EngineAPI.getActiveGame().isDamageFall()) {
            e.setCancelled(true);
            return;
        }
    }


    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerDamageEvent.getHandlerList().unregister(instance);
    }
}

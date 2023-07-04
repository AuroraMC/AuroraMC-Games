/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.util.listeners.defaults;

import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerRegainHealthEvent;
import net.auroramc.engine.api.EngineAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HealthSetting implements Listener {

    private final static HealthSetting instance;

    static {
        instance = new HealthSetting();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHealth(PlayerRegainHealthEvent e) {
        if (EngineAPI.getActiveGame().getHealth() != -1) {
            e.setAmount(0);
            e.getPlayer().setHealth(EngineAPI.getActiveGame().getHealth());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (EngineAPI.getActiveGame().getHealth() != -1) {
                e.setDamage(0);
                ((Player) e.getEntity()).setHealth(EngineAPI.getActiveGame().getHealth());
            }
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(instance, EngineAPI.getGameEngine());
    }

    public static void unregister() {
        PlayerDropItemEvent.getHandlerList().unregister(instance);
    }
}

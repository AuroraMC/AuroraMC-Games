/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.kits.Fighter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            AuroraMCGamePlayer killed = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            AuroraMCGamePlayer killer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getDamager());
            if (killer.getKit() instanceof Fighter) {
                switch (killer.getKitLevel().getLatestUpgrade()) {
                    case 1: {
                        e.setDamage(e.getDamage() * 0.99);
                        break;
                    }
                    case 2: {
                        e.setDamage(e.getDamage() * 0.98);
                        break;
                    }
                    case 3: {
                        e.setDamage(e.getDamage() * 0.97);
                        break;
                    }
                    case 4: {
                        e.setDamage(e.getDamage() * 0.96);
                        break;
                    }
                    case 5: {
                        e.setDamage(e.getDamage() * 0.95);
                        break;
                    }

                }
            } else if (killed.getKit() instanceof Fighter) {
                switch (killer.getKitLevel().getLatestUpgrade()) {
                    case 4:
                    case 3: {
                        e.setDamage(e.getDamage() + 1);
                        break;
                    }
                    case 5: {
                        e.setDamage(e.getDamage() + 2);
                        break;
                    }

                }
            }
        }
    }

}

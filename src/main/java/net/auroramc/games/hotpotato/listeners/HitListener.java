/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.hotpotato.entities.Potato;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HitListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamager() instanceof Player) {
            e.setDamage(0);
            AuroraMCPlayer pl = AuroraMCAPI.getPlayer((Player) e.getDamager());
            if (pl instanceof AuroraMCGamePlayer) {
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
                if (!player.isSpectator() && !player.isVanished()) {
                    if (player.getGameData().containsKey("potato_holder")) {
                        Potato potato = (Potato) player.getGameData().get("potato_holder");
                        AuroraMCGamePlayer newHolder = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
                        potato.newHolder(newHolder);
                    }
                } else {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof EntityDamageByEntityEvent)) {
            e.setCancelled(true);
        }
    }
}

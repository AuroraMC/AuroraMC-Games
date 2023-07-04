/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.run.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

public class LeapListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (!player.isSpectator() && e.getItem() != null && e.getItem().getType() == Material.DIAMOND_AXE && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                if (player.getGameData().containsKey("leapLastUsed")) {
                    double amount = (((long)player.getGameData().get("leapLastUsed") + 30000) - System.currentTimeMillis()) / 100d;
                    long amount1 = Math.round(amount);
                    if (amount1 < 0) {
                        amount1 = 0;
                    }
                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use your leap for **" + (amount1 / 10f) + " seconds**"));
                    return;
                }
                e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(1.25));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 1, 100);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You used **Leap**."));
                player.getGameData().put("leapLastUsed", System.currentTimeMillis());
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "leapsUsed", 1, true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.getGameData().remove("leapLastUsed");
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can now use **Leap**."));
                    }
                }.runTaskLater(ServerAPI.getCore(), 599);

            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(!EngineAPI.getActiveGame().isItemDrop());
    }

}

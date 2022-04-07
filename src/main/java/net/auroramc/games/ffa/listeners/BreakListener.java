/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (!player.isSpectator() && e.getClickedBlock() != null && e.getClickedBlock().getType() != Material.AIR && e.getClickedBlock().getType() != Material.BEDROCK && e.getClickedBlock().getType() != Material.BARRIER && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                e.getClickedBlock().setType(Material.AIR);
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
            }
            if (!player.isSpectator() && e.getItem() != null && e.getItem().getType() == Material.DIAMOND_AXE && e.getAction() == Action.RIGHT_CLICK_AIR) {
                e.setCancelled(true);
                if (player.getGameData().containsKey("leapLastUsed")) {
                    double amount = (((long)player.getGameData().get("leapLastUsed") + 7000) - System.currentTimeMillis()) / 100d;
                    long amount1 = Math.round(amount);
                    if (amount1 < 0) {
                        amount1 = 0;
                    }
                    e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use your leap for **" + (amount1 / 10f) + " seconds**"));
                    return;
                }
                e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(1.25).setY(0.5));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 1, 100);
                e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You used **Leap**."));
                player.getGameData().put("leapLastUsed", System.currentTimeMillis());
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "leapsUsed", 1, true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.getGameData().remove("leapLastUsed");
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can now use **Leap**."));
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 139);

            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}

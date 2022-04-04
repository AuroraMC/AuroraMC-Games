/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() != Material.AIR && e.getClickedBlock().getType() != Material.BEDROCK && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                e.getClickedBlock().setType(Material.AIR);
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
            }
            if (e.getItem() != null && e.getItem().getType() == Material.DIAMOND_AXE && e.getAction() == Action.RIGHT_CLICK_AIR) {
                e.setCancelled(true);
                if (player.getGameData().containsKey("leapLastUsed")) {
                    double amount = ((long)player.getGameData().get("leapLastUsed") - System.currentTimeMillis()) / 100d;
                    long amount1 = Math.round(amount);
                    if (amount < 0) {
                        amount = 0;
                    }
                    e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use your leap for **" + (amount1 / 10f) + " seconds**"));
                    return;
                }
                e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(4).setY(4).normalize());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 1, 100);
                e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You used **Leap**."));
                player.getGameData().put("leapLastUsed", System.currentTimeMillis());
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "leapsUsed", 1, true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.getGameData().remove("leapLastUsed");
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

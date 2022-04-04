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
import org.bukkit.util.BlockIterator;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() != Material.AIR && e.getClickedBlock().getType() != Material.BEDROCK && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                e.getClickedBlock().setType(Material.AIR);
            }
            AuroraMCGamePlayer gamePlayer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (e.getItem() != null && e.getItem().getType() == Material.DIAMOND_AXE) {
                e.setCancelled(true);
                if (gamePlayer.getGameData().containsKey("leapLastUsed")) {
                    double amount = (System.currentTimeMillis() - (long)gamePlayer.getGameData().get("leapLastUsed")) / 100d;
                    long amount1 = Math.round(amount);
                    if ((System.currentTimeMillis() - (long)gamePlayer.getGameData().get("leapLastUsed")) <= 7000) {
                        e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use your leap for **" + (amount1 / 10f) + " seconds**"));
                        return;
                    }
                }
                e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(2).setY(2).normalize());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 1, 100);
                e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You used **Leap**."));
                gamePlayer.getGameData().put("leapLastUsed", System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}

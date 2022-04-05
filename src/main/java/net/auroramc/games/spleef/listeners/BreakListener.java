/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class BreakListener implements Listener {

    private final Material material;

    public BreakListener(Material material) {
        this.material = material;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
        } else {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (e.getBlock().getType() != material) {
                e.setCancelled(true);
            } else {
                if (!player.isSpectator()) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                    if (e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getType() == Material.AIR) {
                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(126))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(126), 1, true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity().getShooter());
            Location location = e.getEntity().getLocation();
            BlockIterator iterator = new BlockIterator(location.getWorld(), location.toVector(), e.getEntity().getVelocity().normalize(), 0, 2);
            while (iterator.hasNext()) {
                Block hitBlock = iterator.next();
                if (hitBlock != null && hitBlock.getType() == material) {
                    hitBlock.setType(Material.AIR);
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                    if (player.getGameData().containsKey("blocksBroken")) {
                        player.getGameData().put("blocksBroken", (int)player.getGameData().get("blocksBroken") + 1);
                        if ((int)player.getGameData().get("blocksBroken") >= 300 && !player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(127))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(127), 1, true);
                        }
                    }
                    if (material == Material.SNOW_BLOCK) {
                        player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "snowBlocksBroken", 1, true);
                        if (player.getStats().getStatistic(EngineAPI.getActiveGameInfo().getId(), "snowBlocksBroken") >= 50000 && !player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(123))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(123), 1, true);
                        }
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerThrow(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity().getShooter());
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "snowballsThrown", 1, true);
            player.getStats().addProgress(AuroraMCAPI.getAchievement(121), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(121), 0), true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}

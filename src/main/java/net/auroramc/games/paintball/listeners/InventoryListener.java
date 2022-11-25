/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.gui.Shop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.List;

public class InventoryListener implements Listener {

    public static BukkitTask runnable = null;

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent e) {
        AuroraMCPlayer pl = AuroraMCAPI.getPlayer((Player) e.getWhoClicked());
        if (AuroraMCAPI.getGUI(pl) == null || e.getInventory() instanceof PlayerInventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        AuroraMCPlayer pl = AuroraMCAPI.getPlayer(e.getPlayer());

        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            return;
        }

        if (e.getItem() != null) {
            if (e.getItem().getType() == Material.GOLD_NUGGET) {
                e.setCancelled(true);
                Shop shop = new Shop(pl);
                shop.open(pl);
                AuroraMCAPI.openGUI(pl, shop);
            } else if (e.getItem().getType() == Material.GOLD_BARDING) {
                if (e.getClickedBlock() != null) {
                    Location location = e.getClickedBlock().getLocation();
                    location.setY(e.getClickedBlock().getY() + 1);
                    new Turret(pl, location);
                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                }
            } else if (e.getItem().getType() == Material.FIREWORK && runnable == null) {
                if (e.getClickedBlock() != null) {
                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + e.getPlayer().getName() + "** has called a Missile Strike, take cover immediately!"));
                    }
                    runnable = new BukkitRunnable(){
                        int i = 0;

                        @Override
                        public void run() {
                            if (i < 10) {
                                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 1);
                                }
                                i++;
                            } else {
                                int y = EngineAPI.getActiveMap().getHighY();
                                for (int x = EngineAPI.getActiveMap().getLowX();x < EngineAPI.getActiveMap().getHighX();x+=5) {
                                    for (int z = EngineAPI.getActiveMap().getLowZ();z < EngineAPI.getActiveMap().getHighZ();z+=5) {
                                        Snowball snowball = EngineAPI.getMapWorld().spawn(new Location(EngineAPI.getMapWorld(), x, y, z), Snowball.class);
                                        snowball.setShooter(null);
                                        snowball.setVelocity(new Vector(0, -1, 0).normalize());
                                    }
                                }
                                runnable = null;
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(AuroraMCAPI.getCore(), 0, 10);
                }
            }
        }
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg && e.getEntity().getShooter() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity().getShooter());
            Location location = e.getEntity().getLocation();
            BlockIterator iterator = new BlockIterator(location.getWorld(), location.toVector(), e.getEntity().getVelocity().normalize(), 0, 2);
            while (iterator.hasNext()) {
                Block hitBlock = iterator.next();
                if (hitBlock != null) {
                    Location loc = hitBlock.getLocation();
                    loc.setY(loc.getY() + 1);
                    Item item = loc.getWorld().dropItem(loc, new ItemStack(Material.EGG, 1));
                    item.setPickupDelay(Integer.MAX_VALUE);
                    Firework firework = loc.getWorld().spawn(loc, Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.setPower(0);
                    meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).trail(true).flicker(true).with(FireworkEffect.Type.BURST).build());
                    firework.setFireworkMeta(meta);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            firework.detonate();
                        }
                    }.runTaskLater(AuroraMCAPI.getCore(), 2);
                    List<Entity> players = item.getNearbyEntities(10, 10, 10);
                    for (Entity entity : players) {
                        if (entity instanceof Player) {
                            ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 0, true, false));
                        }
                    }
                    loc.getWorld().playEffect(loc, Effect.LARGE_SMOKE, 5);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            item.remove();
                        }
                    }.runTaskLater(EngineAPI.getGameEngine(), 140);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onChickenSpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Chicken && !((Ageable)e.getEntity()).isAdult()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    public static BukkitTask getRunnable() {
        return runnable;
    }
}

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.gui.Shop;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import java.util.List;

public class InventoryListener implements Listener {

    private static BukkitTask runnable = null;
    private static int round = 0;

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent e) {
        AuroraMCServerPlayer pl = ServerAPI.getPlayer((Player) e.getWhoClicked());
        if (ServerAPI.getGUI(pl) == null || e.getInventory() instanceof PlayerInventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        AuroraMCServerPlayer pl = e.getPlayer();

        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            return;
        }

        if (e.getItem() != null) {
            if (e.getItem().getType() == Material.GOLD_NUGGET) {
                e.setCancelled(true);
                Shop shop = new Shop(pl);
                shop.open(pl);
            } else if (e.getItem().getType() == Material.GOLD_BARDING) {
                if (e.getClickedBlock() != null) {
                    Location location = e.getClickedBlock().getLocation();
                    location.setY(e.getClickedBlock().getY() + 1);
                    new Turret(pl, location);
                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                }
            } else if (e.getItem().getType() == Material.FIREWORK && runnable == null) {
                e.setCancelled(true);
                if (e.getClickedBlock() != null) {
                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Missile Strike Used").put("player", e.getPlayer().getName())));
                    for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "**" + e.getPlayer().getName() + "** has called a Missile Strike, take cover immediately!"));
                        TextComponent title = new TextComponent("MISSILE STRIKE");
                        title.setColor(ChatColor.DARK_RED.asBungee());
                        title.setBold(true);

                        TextComponent subtitle = new TextComponent("TAKE COVER IMMEDIATELY");
                        title.setColor(ChatColor.LIGHT_PURPLE.asBungee());
                        title.setBold(true);


                        player.sendTitle(title, subtitle, 20, 100, 20);
                    }
                    runnable = new BukkitRunnable(){
                        int i = 0;

                        @Override
                        public void run() {
                            if (i < 10) {
                                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 1);
                                }
                                i++;
                            } else {
                                runnable = null;
                                newRound();
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(ServerAPI.getCore(), 0, 10);
                }
            } else if (e.getItem().getType() == Material.FIREWORK) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use a Missile Strike while one is in progress!"));
            }
        }
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg && e.getEntity().getShooter() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) ServerAPI.getPlayer((Player) e.getEntity().getShooter());
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
                    }.runTaskLater(ServerAPI.getCore(), 2);
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

    public static void newRound() {
        runnable = new BukkitRunnable(){
            @Override
            public void run() {
                int y = EngineAPI.getActiveMap().getHighY();
                for (int x = EngineAPI.getActiveMap().getLowX();x < EngineAPI.getActiveMap().getHighX();x+=(5 - (round*1.5))) {
                    for (int z = EngineAPI.getActiveMap().getLowZ();z < EngineAPI.getActiveMap().getHighZ();z+=(5 - (round*1.5))) {
                        Snowball snowball = EngineAPI.getMapWorld().spawn(new Location(EngineAPI.getMapWorld(), x, y, z), Snowball.class);
                        snowball.setShooter(null);
                        snowball.setVelocity(new Vector(0, -1, 0).normalize());
                    }
                }
                if (round == 2) {
                    round = 0;
                    runnable = null;
                    this.cancel();
                } else {
                    round++;
                    InventoryListener.newRound();
                }
            }

        }.runTaskLater(EngineAPI.getGameEngine(), 30);
    }
}

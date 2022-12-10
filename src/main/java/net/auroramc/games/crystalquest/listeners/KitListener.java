/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.kits.Archer;
import net.auroramc.games.crystalquest.kits.Fighter;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class KitListener implements Listener {

    private final ItemStack stack = new GUIItem(Material.ARROW, "&eArchers Arrow").getItem();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            AuroraMCGamePlayer killed = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            AuroraMCGamePlayer killer = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getDamager());
            if (killer.isVanished() || killer.isSpectator()) {
                e.setCancelled(true);
                return;
            }
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            if (player != null && !player.isSpectator() && player.getKit() instanceof Archer && EngineAPI.getServerState() == ServerState.IN_GAME) {
                if (e.getItem() != null && e.getItem().getType() == Material.BOW && e.getPlayer().getInventory().contains(stack)) {
                    if (player.getGameData().containsKey("last_quickshot")) {
                        int cooldown = 45000 - (player.getKitLevel().getLatestUpgrade() * 1000);
                        if (System.currentTimeMillis() - (long)player.getGameData().get("last_quickshot") < cooldown) {
                            double amount = (((long)player.getGameData().get("last_quickshot") + cooldown) - System.currentTimeMillis()) / 100d;
                            long amount1 = Math.round(amount);
                            if (amount1 < 0) {
                                amount1 = 0;
                            }
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use **Quickshot** for **" + (amount1 / 10f) + "** seconds!"));
                            return;
                        }
                    }
                    player.getGameData().put("last_quickshot", System.currentTimeMillis());
                    AtomicInteger amount = new AtomicInteger();
                    e.getPlayer().getInventory().all(stack).values().forEach(itemStack -> amount.addAndGet(itemStack.getAmount()));
                    ItemStack s = stack.clone();
                    s.setAmount(amount.get());
                    e.getPlayer().getInventory().remove(s);
                    e.getPlayer().launchProjectile(Arrow.class);
                    amount.decrementAndGet();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            e.getPlayer().launchProjectile(Arrow.class);
                            if (amount.decrementAndGet() >= 0 || EngineAPI.getServerState() != ServerState.IN_GAME) {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(EngineAPI.getGameEngine(), 10, 10);
                }
            }
        }
    }

}

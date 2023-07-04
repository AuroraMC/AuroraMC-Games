/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.crystalquest.kits.Archer;
import net.auroramc.games.crystalquest.kits.Fighter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class KitListener implements Listener {

    private final ItemStack stack = new GUIItem(Material.ARROW, "&eArcher's Arrow").getItemStack();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(PlayerDamageByPlayerEvent e) {
        AuroraMCGamePlayer killed = (AuroraMCGamePlayer) e.getPlayer();
        AuroraMCGamePlayer killer = (AuroraMCGamePlayer) e.getDamager();
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (!player.isSpectator() && player.getKit() instanceof Archer && EngineAPI.getServerState() == ServerState.IN_GAME) {
                if (e.getItem() != null && e.getItem().getType() == Material.BOW && e.getPlayer().getInventory().containsAtLeast(stack, 1)) {
                    if (player.getGameData().containsKey("last_quickshot")) {
                        int cooldown = 45000 - (player.getKitLevel().getLatestUpgrade() * 1000);
                        if (System.currentTimeMillis() - (long)player.getGameData().get("last_quickshot") < cooldown) {
                            double amount = (((long)player.getGameData().get("last_quickshot") + cooldown) - System.currentTimeMillis()) / 100d;
                            long amount1 = Math.round(amount);
                            if (amount1 < 0) {
                                amount1 = 0;
                            }
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use **Quickshot** for **" + (amount1 / 10f) + "** seconds!"));
                            return;
                        }
                    }
                    player.getGameData().put("last_quickshot", System.currentTimeMillis());
                    AtomicInteger amount = new AtomicInteger(0);
                    e.getPlayer().getInventory().all(Material.ARROW).values().forEach(itemStack -> amount.addAndGet(itemStack.getAmount()));
                    ItemStack s = stack.clone();
                    s.setAmount(amount.get());
                    e.getPlayer().getInventory().remove(s);
                    Arrow arrow = e.getPlayer().launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(1.2));
                    player.playSound(player.getEyeLocation(), Sound.SHOOT_ARROW, 1, 100);
                    amount.decrementAndGet();
                    if (amount.get() > 0) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                Arrow arrow = e.getPlayer().launchProjectile(Arrow.class);
                                arrow.setVelocity(arrow.getVelocity().multiply(1.2));
                                player.playSound(player.getEyeLocation(), Sound.SHOOT_ARROW, 1, 100);
                                if (amount.decrementAndGet() <= 0 || EngineAPI.getServerState() != ServerState.IN_GAME) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 2, 2);
                    }
                }
            }
        }
    }

}

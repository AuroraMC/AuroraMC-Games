/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.tag.kits.Blinker;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class ItemListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
        if (player != null && !player.isSpectator() && player.getKit() instanceof Blinker && EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
            if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
                e.setCancelled(true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        e.getPlayer().updateInventory();
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 2);
                if (player.getGameData().containsKey("last_blink")) {
                    int cooldown = 40000;
                    switch (player.getKitLevel().getLatestUpgrade()) {
                        case 5:
                            cooldown -= 2500;
                        case 4:
                            cooldown -= 1;
                        case 3:
                            cooldown -= 1;
                        case 2:
                            cooldown -= 2;
                        case 1:
                            cooldown -= 1;
                    }
                    if (System.currentTimeMillis() - (long)player.getGameData().get("last_blink") < cooldown) {
                        double amount = (((long)player.getGameData().get("last_blink") + cooldown) - System.currentTimeMillis()) / 100d;
                        long amount1 = Math.round(amount);
                        if (amount1 < 0) {
                            amount1 = 0;
                        }
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You cannot use **Blink** for **" + (amount1 / 10f) + "** seconds!"));
                        return;
                    }
                }
                player.getGameData().put("last_quickshot", System.currentTimeMillis());
                JSONArray spawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");

                JSONObject spawn = spawns.getJSONObject(new Random().nextInt(spawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getPlayer().getLocation().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(), Effect.PORTAL, 0, 1);
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }
    }

}

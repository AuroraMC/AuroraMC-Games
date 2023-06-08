/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class ItemListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
        if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
            e.setCancelled(true);
            if (player != null && !player.isSpectator() && player.getKit() instanceof Blinker && EngineAPI.getServerState() == ServerState.IN_GAME && !EngineAPI.getActiveGame().isStarting()) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        e.getPlayer().updateInventory();
                    }
                }.runTaskLater(ServerAPI.getCore(), 2);
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
                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use **Blink** for **" + (amount1 / 10f) + "** seconds!"));
                        return;
                    }
                }
                player.getGameData().put("last_blink", System.currentTimeMillis());
                JSONArray spawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");

                JSONObject spawn = spawns.getJSONObject(new Random().nextInt(spawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                player.getLocation().getWorld().playEffect(player.getLocation(), Effect.PORTAL, 0, 1);
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }
    }

}

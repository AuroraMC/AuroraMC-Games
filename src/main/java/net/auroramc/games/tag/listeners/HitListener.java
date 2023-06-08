/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.tag.teams.RunnersTeam;
import net.auroramc.games.tag.teams.TaggedTeam;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HitListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(PlayerDamageByPlayerEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }
        e.setDamage(0);
        AuroraMCServerPlayer pl = e.getDamager();
        if (pl instanceof AuroraMCGamePlayer) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
            AuroraMCGamePlayer hit = (AuroraMCGamePlayer) e.getPlayer();
            if (!player.isSpectator() && !player.isVanished() && player.getTeam() instanceof TaggedTeam && hit.getTeam() instanceof RunnersTeam) {
                e.setDamage(0);
                hit.setTeam(player.getTeam());
                hit.setKit(EngineAPI.getActiveGame().getKits().get(0));
                hit.getInventory().setItem(8, new ItemStack(Material.AIR));
                hit.sendMessage(TextFormatter.pluginMessage("Game", "You were tagged!"));
                player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tags", 1, true);
                hit.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tagged", 1, true);

                hit.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET, null, 1, null, (short) 0, false, Color.fromRGB(255, 0, 0)).getItemStack());
                hit.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short) 0, false, Color.fromRGB(255, 0, 0)).getItemStack());
                hit.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short) 0, false, Color.fromRGB(255, 0, 0)).getItemStack());
                hit.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short) 0, false, Color.fromRGB(255, 0, 0)).getItemStack());

                Firework firework = hit.getLocation().getWorld().spawn(hit.getEyeLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(0);
                meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 0)).trail(true).flicker(true).with(FireworkEffect.Type.BURST).build());
                firework.setFireworkMeta(meta);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(ServerAPI.getCore(), 2);

                KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));

                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.updateNametag(hit);
                    player1.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player1, player, hit, null, KillMessage.KillReason.TAG, EngineAPI.getActiveGameInfo().getId())));
                }
                EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.DEATH, new JSONObject().put("player", hit.getByDisguiseName()).put("killer", player.getByDisguiseName()).put("final", true)));
                List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(pl2 -> !((AuroraMCGamePlayer) pl2).isSpectator() && !(pl2.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
                if (playersAlive.size() == 1) {
                    EngineAPI.getActiveGame().end(playersAlive.get(0));
                }
            } else if (!player.isSpectator() && !player.isVanished() && player.getTeam().equals(hit.getTeam()) && player.getKit() instanceof net.auroramc.games.tag.kits.Player) {
                e.setDamage(0);
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(PlayerDamageEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof PlayerDamageByPlayerEvent)) {
            e.setCancelled(true);
        }

        if (e.getCause() == PlayerDamageEvent.DamageCause.VOID) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            player.sendMessage(TextFormatter.pluginMessage("Game", "You went outside of the border so was teleported back to spawn."));
            JSONArray playerSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYER");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = playerSpawns.getJSONObject(new Random().nextInt(playerSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }
    }

}

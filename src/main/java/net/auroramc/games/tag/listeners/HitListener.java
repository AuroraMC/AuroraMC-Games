/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import net.auroramc.games.tag.teams.RunnersTeam;
import net.auroramc.games.tag.teams.TaggedTeam;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME || EngineAPI.getActiveGame().isStarting()) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamager() instanceof Player) {
            e.setDamage(0);
            AuroraMCPlayer pl = AuroraMCAPI.getPlayer((Player) e.getDamager());
            if (pl instanceof AuroraMCGamePlayer) {
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
                AuroraMCGamePlayer hit = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
                if (!player.isSpectator() && !player.isVanished() && player.getTeam() instanceof TaggedTeam && hit.getTeam() instanceof RunnersTeam) {
                    e.setDamage(0);
                    hit.setTeam(player.getTeam());
                    hit.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You were tagged!"));
                    player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tags", 1, true);
                    hit.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "tagged", 1, true);

                    hit.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItem());
                    hit.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItem());
                    hit.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItem());
                    hit.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItem());

                    Firework firework = hit.getPlayer().getLocation().getWorld().spawn(hit.getPlayer().getEyeLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.setPower(0);
                    meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 0)).trail(true).flicker(true).with(FireworkEffect.Type.BURST).build());
                    firework.setFireworkMeta(meta);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            firework.detonate();
                        }
                    }.runTaskLater(AuroraMCAPI.getCore(), 2);

                    KillMessage killMessage = (KillMessage) player.getActiveCosmetics().getOrDefault(Cosmetic.CosmeticType.KILL_MESSAGE, AuroraMCAPI.getCosmetics().get(500));

                    for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                        player1.updateNametag(hit);
                        player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(player1, player, hit, null, KillMessage.KillReason.TAG, EngineAPI.getActiveGameInfo().getId())));
                    }
                    List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(pl2 -> !((AuroraMCGamePlayer) pl2).isSpectator() && !(pl2.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
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
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (EngineAPI.getServerState() != ServerState.IN_GAME) {
            e.setCancelled(true);
            return;
        }
        if (!(e instanceof EntityDamageByEntityEvent)) {
            e.setCancelled(true);
        }

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity() instanceof Player) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getEntity());
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You went outside of the border so was teleported back to spawn."));
            JSONArray playerSpawns = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("PLAYER");
            if (player.isSpectator()) {
                JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                JSONObject spawn = playerSpawns.getJSONObject(new Random().nextInt(playerSpawns.length()));
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                player.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }
    }

}

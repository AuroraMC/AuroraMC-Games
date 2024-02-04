/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.tag;

import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.*;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.tag.kits.Blinker;
import net.auroramc.games.tag.listeners.HitListener;
import net.auroramc.games.tag.listeners.ItemListener;
import net.auroramc.games.tag.teams.RunnersTeam;
import net.auroramc.games.tag.teams.TaggedTeam;
import net.auroramc.games.tag.utils.TagScoreboardRunnable;
import net.auroramc.games.util.listeners.settings.*;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tag extends Game {


    public Tag(GameVariationInfo gameVariation) {
        super(gameVariation);
    }

    private TagScoreboardRunnable runnable;
    private HitListener hitListener;
    private ItemListener itemListener;


    @Override
    public void preLoad() {
        this.teams.put("players", new RunnersTeam());
        this.kits.add(new net.auroramc.games.tag.kits.Player());
        this.kits.add(new Blinker());
        runnable = new TagScoreboardRunnable();
        itemListener = new ItemListener();
        hitListener = new HitListener();

        itemDrop = false;
        itemPickup = false;
        blockBreak = false;
        blockPlace = false;
    }

    @Override
    public void load(GameMap gameMap) {
        this.map = gameMap;
    }

    @Override
    public void start() {
        super.start();
        int spawnIndex = 0;
        JSONArray spawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            gp.getScoreboard().clear();
            gp.getScoreboard().setTitle("&3&l-= &b&lTAG &3&l=-");
            if (gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y + 0.5, z, yaw, 0));
            } else {
                JSONObject spawn = spawns.getJSONObject(spawnIndex);
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                spawnIndex++;
                if (spawnIndex >= spawns.length()) {
                    spawnIndex = 0;
                }
                gp.getKit().onGameStart(player);
            }
        }
        Bukkit.getPluginManager().registerEvents(hitListener, ServerAPI.getCore());
        Bukkit.getPluginManager().registerEvents(itemListener, ServerAPI.getCore());
        DisableBreakListener.register();
        DisableHungerListener.register();
        DisablePlaceListener.register();
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableMovableItems.register();
        DisableWeatherListener.register();
        runnable.runTaskTimer(ServerAPI.getCore(), 0, 20);
    }

    @Override
    public void end(AuroraMCServerPlayer winner) {
        end();
        super.end(winner);
    }

    @Override
    public void end(Team winner, String winnerName) {
        end();
        super.end(winner, winnerName);
    }

    @Override
    public void generateTeam(AuroraMCServerPlayer auroraMCPlayer) {
    }

    @Override
    public void inProgress() {
        super.inProgress();
        TaggedTeam team = new TaggedTeam();
        this.teams.put("Tagged", team);
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        int random = new Random().nextInt(playersAlive.size());
        AuroraMCServerPlayer player = playersAlive.get(random);
        player.setTeam(team);

        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.updateNametag(player);
                    if (player1.equals(player)) {
                        if (player1.isDisguised()) {
                            if (player1.getPreferences().isHideDisguiseNameEnabled()) {
                                player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                                continue;
                            }
                        }
                        player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                    } else {
                        player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                    }
                }
                player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItemStack());
                player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItemStack());
                player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItemStack());
                player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short)0,false, Color.fromRGB(255, 0, 0)).getItemStack());
                Firework firework = player.getLocation().getWorld().spawn(player.getEyeLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(0);
                meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 0)).trail(true).flicker(true).with(FireworkEffect.Type.BURST).build());
                firework.setFireworkMeta(meta);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(ServerAPI.getCore(), 2);
            }
        }.runTask(ServerAPI.getCore());
    }

    private void end() {
        PlayerDamageByPlayerEvent.getHandlerList().unregister(hitListener);
        PlayerDamageEvent.getHandlerList().unregister(hitListener);
        PlayerInteractEvent.getHandlerList().unregister(itemListener);
        DisablePlaceListener.unregister();
        DisableBreakListener.unregister();
        DisableHungerListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableMovableItems.unregister();
        DisableWeatherListener.unregister();
        runnable.cancel();
    }

    @Override
    public void onPlayerJoin(Player player) {
        JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
        int x, y, z;
        x = specSpawn.getInt("x");
        y = specSpawn.getInt("y");
        z = specSpawn.getInt("z");
        float yaw = specSpawn.getFloat("yaw");
        player.teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(player);
                }
            }
        }.runTaskLater(ServerAPI.getCore(), 2);
    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {
        if (!auroraMCGamePlayer.isVanished()) {
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Spectator Joined").put("player", auroraMCGamePlayer.getByDisguiseName())));
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                    player2.hidePlayer(auroraMCGamePlayer);
                }
                auroraMCGamePlayer.setSpectator(true, true);
            }
        }.runTask(ServerAPI.getCore());

    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {
        if (!auroraMCGamePlayer.isVanished()) {
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Player Leave").put("player", auroraMCGamePlayer.getByDisguiseName())));
        }
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && !(player.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
        if (auroraMCGamePlayer.getTeam() instanceof TaggedTeam) {
            List<AuroraMCServerPlayer> tagged = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && (player.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
            if (tagged.size() == 0) {
                int random = new Random().nextInt(playersAlive.size());
                AuroraMCServerPlayer player = playersAlive.get(random);
                player.setTeam(this.teams.get("Tagged"));
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.updateNametag(player);
                    if (player1.equals(player)) {
                        if (player1.isDisguised()) {
                            if (player1.getPreferences().isHideDisguiseNameEnabled()) {
                                player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                                continue;
                            }
                        }
                        player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                    } else {
                        player1.sendMessage(TextFormatter.pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                    }
                }
            }
        }
        if (playersAlive.size() == 1) {
            this.end(playersAlive.get(0));
        }
    }

    @Override
    public void onRespawn(AuroraMCGamePlayer gp) {
        JSONArray spawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
        JSONObject spawn = spawns.getJSONObject(new Random().nextInt(spawns.length()));
        int x, y, z;
        x = spawn.getInt("x");
        y = spawn.getInt("y");
        z = spawn.getInt("z");
        float yaw = spawn.getFloat("yaw");
        gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
        gp.getKit().onGameStart(gp);
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer killer) {
        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.DEATH, new JSONObject().put("player", auroraMCGamePlayer.getByDisguiseName()).put("killer", ((killer != null)?killer.getByDisguiseName():"None")).put("final", true)));
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).getGameData().containsKey("tagged")).collect(Collectors.toList());
        if (playersAlive.size() == 1) {
            this.end(playersAlive.get(0));
        }
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {
    }
}

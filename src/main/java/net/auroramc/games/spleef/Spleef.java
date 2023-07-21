/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.spleef;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Team;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.core.api.events.entity.FoodLevelChangeEvent;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.*;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.spleef.kits.Snowman;
import net.auroramc.games.spleef.listeners.*;
import net.auroramc.games.spleef.utils.SpleefScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.death.NoDamageInstaKillListener;
import net.auroramc.games.util.listeners.defaults.BlockPlaceSetting;
import net.auroramc.games.util.listeners.settings.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Spleef extends Game {


    public Spleef(GameVariationInfo gameVariation) {
        super(gameVariation);
    }

    private DeathListener deathListener;
    private ItemSpawnListener itemSpawnListener;
    private BreakListener breakListener;
    private WaterListener waterListener;
    private SpleefScoreboardRunnable runnable;


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new Snowman());
        runnable = new SpleefScoreboardRunnable();

        itemDrop = false;
        itemPickup = false;
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
            gp.getScoreboard().setTitle("&3&l-= &b&lSPLEEF &3&l=-");
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
        deathListener = new DeathListener();
        itemSpawnListener = new ItemSpawnListener();
        waterListener = new WaterListener();
        breakListener = new BreakListener(Material.valueOf(this.map.getMapData().getString("block").toUpperCase()));
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableMovableItems.register();
        NoDamageInstaKillListener.register();
        DisableWeatherListener.register();
        DisableHungerListener.register();
        BlockPlaceSetting.register();
        Bukkit.getPluginManager().registerEvents(deathListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(itemSpawnListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(breakListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(waterListener, EngineAPI.getGameEngine());
        runnable.runTaskTimer(ServerAPI.getCore(), 0, 20);
    }

    @Override
    public void end(AuroraMCServerPlayer winner) {
        end();
        if (winner != null) {
            winner.getStats().addProgress(AuroraMCAPI.getAchievement(122), 1, winner.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(122), 0), true);
        }
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

    private void end() {
        ItemSpawnEvent.getHandlerList().unregister(itemSpawnListener);
        BlockBreakEvent.getHandlerList().unregister(breakListener);
        PlayerShowEvent.getHandlerList().unregister(deathListener);
        PlayerDropItemEvent.getHandlerList().unregister(breakListener);
        ProjectileHitEvent.getHandlerList().unregister(breakListener);
        PlayerMoveEvent.getHandlerList().unregister(waterListener);
        NoDamageInstaKillListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableMovableItems.unregister();
        DisableWeatherListener.unregister();
        DisableHungerListener.unregister();
        BlockPlaceSetting.unregister();
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
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        if (playersAlive.size() == 1 || (playersAlive.contains(auroraMCGamePlayer) && playersAlive.size() == 2)) {
            playersAlive.remove(auroraMCGamePlayer);
            EngineAPI.getActiveGame().end(playersAlive.get(0));
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
        if (killer != null) {
            if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(124))) {
                killer.getStats().achievementGained(AuroraMCAPI.getAchievement(124), 1, true);
            }
        }

        if ((System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000 < 3000) {
            if (!auroraMCGamePlayer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(125))) {
                auroraMCGamePlayer.getStats().achievementGained(AuroraMCAPI.getAchievement(125), 1, true);
            }
        }
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        if (playersAlive.size() == 1) {
            this.end(playersAlive.get(0));
        }
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }
}

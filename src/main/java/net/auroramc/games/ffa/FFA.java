/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.ffa;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.*;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.ffa.kits.Berserker;
import net.auroramc.games.ffa.listeners.BreakListener;
import net.auroramc.games.ffa.listeners.ItemSpawnListener;
import net.auroramc.games.ffa.listeners.ShowListener;
import net.auroramc.games.ffa.util.FFAScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.death.DeathListener;
import net.auroramc.games.util.listeners.defaults.HealthSetting;
import net.auroramc.games.util.listeners.defaults.HungerSetting;
import net.auroramc.games.util.listeners.defaults.ItemPickupSetting;
import net.auroramc.games.util.listeners.settings.DisableItemDrop;
import net.auroramc.games.util.listeners.settings.DisableItemPickup;
import net.auroramc.games.util.listeners.settings.DisableRemovableArmor;
import net.auroramc.games.util.listeners.settings.DisableWeatherListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FFA extends Game {


    public FFA(GameVariationInfo gameVariation) {
        super(gameVariation);
    }

    private ShowListener showListener;
    private ItemSpawnListener itemSpawnListener;
    private BreakListener breakListener;
    private FFAScoreboardRunnable runnable;


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new Berserker());
        runnable = new FFAScoreboardRunnable();
        itemDrop = false;
        itemPickup = false;
        super.preLoad();
    }

    @Override
    public void load(GameMap gameMap) {
        this.map = gameMap;
        super.load(gameMap);
    }

    @Override
    public void start() {
        super.start();
        int spawnIndex = 0;
        JSONArray spawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("PLAYERS");
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            if (gp.getKitLevel().getLevel() >= 100) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(149))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(149), 1, true);
                }
            }
            gp.getScoreboard().clear();
            gp.getScoreboard().setTitle("&3&l-= &b&lFFA &3&l=-");
            if (gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
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
        showListener = new ShowListener();
        itemSpawnListener = new ItemSpawnListener();
        breakListener = new BreakListener();
        DeathListener.register(true);
        Bukkit.getPluginManager().registerEvents(showListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(itemSpawnListener, EngineAPI.getGameEngine());
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableRemovableArmor.register();
        DisableWeatherListener.register();
        HealthSetting.register();
        HungerSetting.register();
        Bukkit.getPluginManager().registerEvents(breakListener, EngineAPI.getGameEngine());
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

    private void end() {
        ItemSpawnEvent.getHandlerList().unregister(itemSpawnListener);
        DeathListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableRemovableArmor.unregister();
        DisableWeatherListener.unregister();
        HealthSetting.unregister();
        HungerSetting.unregister();
        PlayerInteractEvent.getHandlerList().unregister(breakListener);
        PlayerShowEvent.getHandlerList().unregister(showListener);
        PlayerDropItemEvent.getHandlerList().unregister(breakListener);
        PlayerDamageByPlayerEvent.getHandlerList().unregister(breakListener);
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (player.getStats().getStatistic(EngineAPI.getActiveGameInfo().getId(), "damageDealt") >= 100000) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(142))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(142), 1, true);
                }
            }
        }
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
        if (auroraMCGamePlayer.hasPermission("admin") && killer != null) {
            if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(143))) {
                killer.getStats().achievementGained(AuroraMCAPI.getAchievement(143), 1, true);
            }
        } else if (auroraMCGamePlayer.hasPermission("moderation") && killer != null) {
            if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(144))) {
                killer.getStats().achievementGained(AuroraMCAPI.getAchievement(144), 1, true);
            }
        }
        if (killer != null) {
            long gametime = (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp()) - 10000;
            if (gametime <= 60000) {
                killer.getGameData().put("killAchieve", (int)killer.getGameData().getOrDefault("killAchieve", 0) + 1);
                if ((int)killer.getGameData().get("killAchieve") >= 3) {
                    if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(150))) {
                        killer.getStats().achievementGained(AuroraMCAPI.getAchievement(150), 1, true);
                    }
                }
            }
            killer.getStats().addProgress(AuroraMCAPI.getAchievement(145), 1, killer.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(145), 0),true);
        }
        if (auroraMCGamePlayer.getStats().getStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths") > 100) {
            if (!auroraMCGamePlayer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(148))) {
                auroraMCGamePlayer.getStats().achievementGained(AuroraMCAPI.getAchievement(148), 1, true);
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

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.ffa;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.ffa.kits.Brawler;
import net.auroramc.games.ffa.listeners.BreakListener;
import net.auroramc.games.ffa.listeners.ItemSpawnListener;
import net.auroramc.games.ffa.listeners.ShowListener;
import net.auroramc.games.ffa.util.FFAScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.death.DeathListener;
import net.auroramc.games.util.listeners.death.NoDamageInstaKillListener;
import net.auroramc.games.util.listeners.settings.DisableHungerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class FFA extends Game {


    public FFA(GameVariation gameVariation) {
        super(gameVariation);
    }

    private ShowListener showListener;
    private ItemSpawnListener itemSpawnListener;
    private BreakListener breakListener;
    private FFAScoreboardRunnable runnable;


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new Brawler());
        runnable = new FFAScoreboardRunnable();
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
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
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
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
            } else {
                JSONObject spawn = spawns.getJSONObject(spawnIndex);
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
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
        DisableHungerListener.register();
        Bukkit.getPluginManager().registerEvents(breakListener, EngineAPI.getGameEngine());
        runnable.runTaskTimer(AuroraMCAPI.getCore(), 0, 20);
    }

    @Override
    public void end(AuroraMCPlayer winner) {
        end();
        super.end(winner);
    }

    @Override
    public void end(Team winner, String winnerName) {
        end();
        super.end(winner, winnerName);
    }

    @Override
    public void generateTeam(AuroraMCPlayer auroraMCPlayer) {
    }

    private void end() {
        ItemSpawnEvent.getHandlerList().unregister(itemSpawnListener);
        DeathListener.unregister();
        PlayerInteractEvent.getHandlerList().unregister(breakListener);
        PlayerShowEvent.getHandlerList().unregister(showListener);
        PlayerDropItemEvent.getHandlerList().unregister(breakListener);
        NoDamageInstaKillListener.unregister();
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
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
        }.runTaskLater(AuroraMCAPI.getCore(), 2);
    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {
        new BukkitRunnable(){
            @Override
            public void run() {
                auroraMCGamePlayer.setSpectator(true, true);
            }
        }.runTask(AuroraMCAPI.getCore());

    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        if (playersAlive.size() == 1) {
            EngineAPI.getActiveGame().end(playersAlive.get(0));
        }
    }

    @Override
    public void onRespawn(AuroraMCGamePlayer auroraMCGamePlayer) {
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer killer) {
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
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        if (playersAlive.size() == 1) {
            this.end(playersAlive.get(0));
        }
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }
}

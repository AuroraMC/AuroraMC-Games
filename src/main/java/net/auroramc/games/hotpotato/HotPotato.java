/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.hotpotato;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.*;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.hotpotato.listeners.HitListener;
import net.auroramc.games.hotpotato.utils.HotPotatoScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.settings.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HotPotato extends Game {

    private int roundLength;

    private int potatoes;
    private HitListener hitListener;
    private HotPotatoScoreboardRunnable runnable;
    private BukkitTask potatoTask;
    private List<Potato> potatoList;
    private int round;


    public HotPotato(GameVariationInfo gameVariation) {
        super(gameVariation);
    }


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new net.auroramc.games.hotpotato.kits.Potato());
        runnable = new HotPotatoScoreboardRunnable();
        hitListener = new HitListener();
        potatoes = 0;
        potatoList = new ArrayList<>();
        round = 0;
        itemDrop = false;
        itemPickup = false;
        roundLength = 900;
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
            gp.getScoreboard().setTitle("&3&l-= &b&lHOTPOTATO &3&l=-");
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
        Bukkit.getPluginManager().registerEvents(hitListener, EngineAPI.getGameEngine());
        DisablePlaceListener.register();
        DisableBreakListener.register();
        DisableHungerListener.register();
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableMovableItems.register();
        DisableWeatherListener.register();
        runnable.runTaskTimer(ServerAPI.getCore(), 0, 20);
    }

    @Override
    public void end(AuroraMCServerPlayer winner) {
        end();
        if (winner != null) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) winner;
            if ((int)gp.getGameData().getOrDefault("total_potato_rounds", 0) == round) {
                if (!gp.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(163))) {
                    gp.getStats().achievementGained(AuroraMCAPI.getAchievement(163), 1, true);
                }
            }
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

    @Override
    public void inProgress() {
        super.inProgress();
        new BukkitRunnable(){
            @Override
            public void run() {
                generatePotatoes();
            }
        }.runTask(ServerAPI.getCore());
    }

    public void generatePotatoes() {
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            if (!gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("game").getJSONArray("MIDDLE").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            }
        }
        round++;
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer) player).isSpectator()).collect(Collectors.toList());
        Collections.shuffle(playersAlive);
        potatoes = Math.round(playersAlive.size() / 4f);
        if (potatoes < 1) {
            potatoes = 1;
        }

        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", potatoes + " Potatoes Released")));
        for (int i = 0;i < potatoes;i++) {
            Potato potato = new Potato();
            AuroraMCServerPlayer pl = playersAlive.remove(0);
            potato.newHolder((AuroraMCGamePlayer) pl);
            potatoList.add(potato);
        }
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "**" + potatoes + "** Hot Potato" + ((potatoes > 1)?"es":"") + " have been released! They explode in **45** seconds!"));
        }
        potatoTask = new BukkitRunnable(){
            @Override
            public void run() {
                explodePotatoes(false);
            }
        }.runTaskLater(ServerAPI.getCore(), roundLength);
    }

    public void explodePotatoes(boolean cancel) {
        if (cancel) potatoTask.cancel();
        for (Potato potato : potatoList) {
            if (round == 1) {
                if (!potato.getHolder().getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(165))) {
                    potato.getHolder().getStats().achievementGained(AuroraMCAPI.getAchievement(165), 1, true);
                }
            }
            potato.getHolder().getStats().addProgress(AuroraMCAPI.getAchievement(168), 1, potato.getHolder().getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(165), 0), true);
            potato.explode();
        }
        potatoList.clear();
        potatoes = 0;
        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Potatoes Exploded")));
        List<AuroraMCServerPlayer> playersAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer) player).isSpectator()).collect(Collectors.toList());
        for (AuroraMCServerPlayer player : playersAlive) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            if (gp.getGameData().containsKey("had_potato")) {
                gp.getGameData().remove("had_potato");
                gp.getGameData().put("total_potato_rounds", (int)gp.getGameData().getOrDefault("total_potato_rounds", 0) + 1);
                gp.getGameData().remove("rounds_without_potato");
            } else {
                gp.getGameData().put("rounds_without_potato",(int)gp.getGameData().getOrDefault("rounds_without_potato", 0) + 1);
                if (!gp.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(162))) {
                    gp.getStats().achievementGained(AuroraMCAPI.getAchievement(162), 1, true);
                }
                if ((int)gp.getGameData().get("rounds_without_potato") >= 3) {
                    if (!gp.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(170))) {
                        gp.getStats().achievementGained(AuroraMCAPI.getAchievement(170), 1, true);
                    }
                }
            }
        }
        if (playersAlive.size() == 1) {
            end(playersAlive.get(0));
            return;
        }
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "The potatoes have exploded! The next round starts in 10 seconds!"));
        }
        potatoTask = new BukkitRunnable() {
            @Override
            public void run() {
                generatePotatoes();
            }
        }.runTaskLater(ServerAPI.getCore(), 200);
    }

    private void end() {
        PlayerDamageByPlayerEvent.getHandlerList().unregister(hitListener);
        PlayerDamageEvent.getHandlerList().unregister(hitListener);
        DisablePlaceListener.unregister();
        DisableBreakListener.unregister();
        DisableHungerListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableMovableItems.unregister();
        DisableWeatherListener.unregister();
        if (potatoTask != null) {
            potatoTask.cancel();
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
        } else {
            if (auroraMCGamePlayer.getGameData().containsKey("potato_holder")) {
                if (potatoes == 1) {
                    potatoTask.cancel();
                    for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "The player with the potato has left! The next round starts in 10 seconds!"));
                    }
                    potatoList.clear();
                    potatoTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            generatePotatoes();
                        }
                    }.runTaskLater(ServerAPI.getCore(), 200);
                } else {
                    Potato potato = (Potato) auroraMCGamePlayer.getGameData().get("potato_holder");
                    potatoList.remove(potato);
                    potatoes--;
                }
            }
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

    public int getPotatoes() {
        return potatoes;
    }
}

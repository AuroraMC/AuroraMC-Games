/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.hotpotato.listeners.HitListener;
import net.auroramc.games.hotpotato.utils.HotPotatoScoreboardRunnable;
import net.auroramc.games.tag.kits.TagKit;
import net.auroramc.games.tag.utils.TagScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.settings.DisableBreakListener;
import net.auroramc.games.util.listeners.settings.DisableHungerListener;
import net.auroramc.games.util.listeners.settings.DisablePlaceListener;
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
import java.util.stream.Collectors;

public class HotPotato extends Game {

    private int potatoes;
    private HitListener hitListener;
    private HotPotatoScoreboardRunnable runnable;
    private BukkitTask potatoTask;
    private List<Potato> potatoList;


    public HotPotato(GameVariation gameVariation) {
        super(gameVariation);
    }


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new TagKit());
        runnable = new HotPotatoScoreboardRunnable();
        hitListener = new HitListener();
        potatoes = 0;
        potatoList = new ArrayList<>();
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
            gp.getScoreboard().clear();
            gp.getScoreboard().setTitle("&3&l-= &b&lHOTPOTATO &3&l=-");
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
        Bukkit.getPluginManager().registerEvents(hitListener, EngineAPI.getGameEngine());
        DisablePlaceListener.register();
        DisableBreakListener.register();
        DisableHungerListener.register();
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

    @Override
    public void inProgress() {
        super.inProgress();
        generatePotatoes();
    }

    public void generatePotatoes() {
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer) player).isSpectator()).collect(Collectors.toList());
        Collections.shuffle(playersAlive);
        potatoes = Math.round(playersAlive.size() / 4f);
        if (potatoes < 1) {
            potatoes = 1;
        }
        for (int i = 0;i < potatoes;i++) {
            Potato potato = new Potato();
            AuroraMCPlayer pl = playersAlive.remove(0);
            potato.newHolder((AuroraMCGamePlayer) pl);
        }
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + potatoes + "** Hot Potatoes have been released! They explode in **45** seconds!"));
        }
        potatoTask = new BukkitRunnable(){
            @Override
            public void run() {
                for (Potato potato : potatoList) {
                    potato.explode();
                }
                potatoList.clear();
                List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer) player).isSpectator()).collect(Collectors.toList());
                if (playersAlive.size() == 1) {
                    end(playersAlive.get(0));
                    return;
                }
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The potatoes have exploded! The next round starts in 10 seconds!"));
                }
                potatoTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        generatePotatoes();
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 200);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 900);
    }

    private void end() {
        EntityDamageByEntityEvent.getHandlerList().unregister(hitListener);
        EntityDamageEvent.getHandlerList().unregister(hitListener);
        DisablePlaceListener.unregister();
        DisableBreakListener.unregister();
        DisableHungerListener.unregister();
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
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).getGameData().containsKey("tagged")).collect(Collectors.toList());
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

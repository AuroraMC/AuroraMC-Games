/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.teams.CQRed;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.kits.Tribute;
import net.auroramc.games.paintball.listeners.HitListener;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import net.auroramc.games.paintball.utils.PaintballScoreboardRunnable;
import net.auroramc.games.tag.teams.TaggedTeam;
import net.auroramc.games.util.listeners.settings.DisableBreakListener;
import net.auroramc.games.util.listeners.settings.DisableHungerListener;
import net.auroramc.games.util.listeners.settings.DisablePlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Paintball extends Game {


    public Paintball(GameVariation gameVariation) {
        super(gameVariation);
    }

    private PaintballScoreboardRunnable runnable;
    private HitListener listener;
    private Map<ArmorStand, Turret> turrets;


    @Override
    public void preLoad() {
        runnable = new PaintballScoreboardRunnable();
        this.teams.put("Red", new PBRed());
        this.teams.put("Blue", new PBBlue());
        this.kits.add(new Tribute());
        this.turrets = new HashMap<>();
    }

    @Override
    public void load(GameMap gameMap) {
        this.map = gameMap;
    }

    @Override
    public void start() {
        super.start();
        int redSpawnIndex = 0;
        int blueSpawnIndex = 0;
        JSONArray redSpawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("RED");
        JSONArray blueSpawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("BLUE");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            gp.getScoreboard().clear();
            gp.getScoreboard().setTitle("&3&l-= &b&lPAINTBALL &3&l=-");
            if (gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                if (gp.getTeam() instanceof PBRed) {
                    JSONObject spawn = redSpawns.getJSONObject(redSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                    redSpawnIndex++;
                    if (redSpawnIndex >= redSpawns.length()) {
                        redSpawnIndex = 0;
                    }
                } else {
                    JSONObject spawn = blueSpawns.getJSONObject(blueSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                    blueSpawnIndex++;
                    if (blueSpawnIndex >= blueSpawns.length()) {
                        blueSpawnIndex = 0;
                    }
                }
                gp.getKit().onGameStart(player);
                for (org.bukkit.scoreboard.Team team : gp.getScoreboard().getScoreboard().getTeams()) {
                    AuroraMCPlayer player1 = AuroraMCAPI.getDisguisedPlayer(team.getName());
                    if (player1 == null) {
                        player1 = AuroraMCAPI.getPlayer(team.getName());
                    }
                    if (!player1.getTeam().equals(gp.getTeam())) {
                        team.setNameTagVisibility(NameTagVisibility.NEVER);
                    }
                }
            }
        }
        runnable.runTaskTimer(AuroraMCAPI.getCore(), 0, 20);
        listener = new HitListener();
        Bukkit.getPluginManager().registerEvents(listener, EngineAPI.getGameEngine());
        DisableHungerListener.register();
        DisableBreakListener.register();
        DisablePlaceListener.register();
        ((PBRed)this.teams.get("Red")).initLives();
        ((PBBlue)this.teams.get("Blue")).initLives();
    }

    @Override
    public void end(AuroraMCPlayer winner) {
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
    public void generateTeam(AuroraMCPlayer auroraMCPlayer) {
    }

    private void end() {
        runnable.cancel();
        EntityDamageByEntityEvent.getHandlerList().unregister(listener);
        EntityDamageEvent.getHandlerList().unregister(listener);
        PlayerArmorStandManipulateEvent.getHandlerList().unregister(listener);
        for (Turret turret : turrets.values()) {
            turret.getTask().cancel();
            turret.getArmorStand().remove();
        }
        DisableHungerListener.unregister();
        DisableBreakListener.unregister();
        DisablePlaceListener.unregister();
    }

    @Override
    public void inProgress() {
        super.inProgress();
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (!((AuroraMCGamePlayer)player).isSpectator()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Turret turret = new Turret(player, player.getPlayer().getLocation());
                    }
                }.runTask(EngineAPI.getGameEngine());
            }
        }
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
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(auroraMCGamePlayer.getPlayer());
                }
                auroraMCGamePlayer.setSpectator(true, true);
            }
        }.runTask(AuroraMCAPI.getCore());

    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {
        if (!auroraMCGamePlayer.isSpectator()) {
            for (Turret turret : new ArrayList<>(turrets.values())) {
                if (turret.getOwner().equals(auroraMCGamePlayer)) {
                    turret.getTask().cancel();
                    turret.getArmorStand().remove();
                    turrets.remove(turret.getArmorStand());
                }
            }
        }
        List<AuroraMCPlayer> blueAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && !(player.getTeam() instanceof PBBlue)).collect(Collectors.toList());
        List<AuroraMCPlayer> redAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && !(player.getTeam() instanceof PBRed)).collect(Collectors.toList());
        if (blueAlive.size() == 0) {
            this.end(this.teams.get("Red"), null);
            return;
        }
        if (redAlive.size() == 0) {
            this.end(this.teams.get("Blue"), null);
            return;
        }
    }

    @Override
    public void onRespawn(AuroraMCGamePlayer auroraMCGamePlayer) {
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer killer) {
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    public Map<ArmorStand, Turret> getTurrets() {
        return turrets;
    }
}

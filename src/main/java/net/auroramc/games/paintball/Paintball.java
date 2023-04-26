/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.entity.PlayerDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.events.player.PlayerArmorStandManipulateEvent;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.entities.Turret;
import net.auroramc.games.paintball.kits.Tribute;
import net.auroramc.games.paintball.listeners.HitListener;
import net.auroramc.games.paintball.listeners.InventoryListener;
import net.auroramc.games.paintball.teams.PBBlue;
import net.auroramc.games.paintball.teams.PBRed;
import net.auroramc.games.paintball.utils.PaintballScoreboardRunnable;
import net.auroramc.games.util.listeners.settings.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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
    private BukkitTask endGameTask;
    private HitListener hitListener;
    private InventoryListener inventoryListener;
    private Map<ArmorStand, Turret> turrets;
    private int livesPerKill;


    @Override
    public void preLoad() {
        runnable = new PaintballScoreboardRunnable();
        this.teams.put("Red", new PBRed());
        this.teams.put("Blue", new PBBlue());
        this.kits.add(new Tribute());
        this.turrets = new HashMap<>();
        livesPerKill = 1;
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
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
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
                gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                gp.getGameData().put("gold", 1);
                gp.setGameMode(GameMode.ADVENTURE);
                if (gp.getTeam() instanceof PBRed) {
                    JSONObject spawn = redSpawns.getJSONObject(redSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
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
                    gp.teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                    blueSpawnIndex++;
                    if (blueSpawnIndex >= blueSpawns.length()) {
                        blueSpawnIndex = 0;
                    }
                }
                gp.getKit().onGameStart(player);
                for (org.bukkit.scoreboard.Team team : gp.getScoreboard().getScoreboard().getTeams()) {
                    AuroraMCServerPlayer player1 = ServerAPI.getDisguisedPlayer(team.getName());
                    if (player1 == null) {
                        player1 = ServerAPI.getPlayer(team.getName());
                    }
                    if (!((AuroraMCGamePlayer)player1).isSpectator() && !player1.getTeam().equals(gp.getTeam())) {
                        team.setNameTagVisibility(NameTagVisibility.NEVER);
                    }
                }
            }
        }
        runnable.runTaskTimer(ServerAPI.getCore(), 0, 20);
        hitListener = new HitListener();
        inventoryListener = new InventoryListener();
        Bukkit.getPluginManager().registerEvents(hitListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(inventoryListener, EngineAPI.getGameEngine());
        DisableHungerListener.register();
        DisableBreakListener.register();
        DisablePlaceListener.register();
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableMovableItems.register();
        DisableWeatherListener.register();
        ((PBRed)this.teams.get("Red")).initLives();
        ((PBBlue)this.teams.get("Blue")).initLives();
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
        if (InventoryListener.getRunnable() != null) {
            InventoryListener.getRunnable().cancel();
        }
        runnable.cancel();
        endGameTask.cancel();
        PlayerDamageByPlayerEvent.getHandlerList().unregister(hitListener);
        PlayerDamageEvent.getHandlerList().unregister(hitListener);
        PlayerArmorStandManipulateEvent.getHandlerList().unregister(hitListener);
        PlayerInteractEvent.getHandlerList().unregister(inventoryListener);
        InventoryInteractEvent.getHandlerList().unregister(inventoryListener);
        ProjectileHitEvent.getHandlerList().unregister(inventoryListener);
        EntitySpawnEvent.getHandlerList().unregister(inventoryListener);
        PlayerDropItemEvent.getHandlerList().unregister(inventoryListener);
        ProjectileLaunchEvent.getHandlerList().unregister(hitListener);
        for (Turret turret : turrets.values()) {
            turret.getTask().cancel();
            turret.getArmorStand().remove();
        }
        DisableHungerListener.unregister();
        DisableBreakListener.unregister();
        DisablePlaceListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableMovableItems.unregister();
        DisableWeatherListener.unregister();
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!((AuroraMCGamePlayer)player).isSpectator() && !starting) {
                ((BukkitTask)((AuroraMCGamePlayer)player).getGameData().get("runnable")).cancel();
            }
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public void inProgress() {
        super.inProgress();
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!((AuroraMCGamePlayer)player).isSpectator()) {
                int interval = 12 * 20;
                if (((AuroraMCGamePlayer) player).getKit() instanceof Tribute) {
                    switch (((AuroraMCGamePlayer) player).getKitLevel().getLatestUpgrade()) {
                        case 5:
                            interval-=40;
                        case 4:
                            interval-=20;
                        case 3:
                            interval-=20;
                        case 2:
                            interval-=20;
                        case 1:
                            interval-=20;
                    }
                }
                ((AuroraMCGamePlayer) player).getGameData().put("runnable", new BukkitRunnable(){
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            if (!player.getInventory().contains(Material.SNOW_BALL, 64)) {
                                if (player.getInventory().getItem(0) != null && player.getInventory().getItem(0).getType() == Material.SNOW_BALL) {
                                    player.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, player.getInventory().getItem(0).getAmount() + 1, null).getItemStack());
                                } else {
                                    player.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, 1, null).getItemStack());
                                }
                            }
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(EngineAPI.getGameEngine(), interval, interval));
            }
        }

        endGameTask = new BukkitRunnable(){
            @Override
            public void run() {
                livesPerKill++;
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.sendMessage(TextFormatter.pluginMessage("Game", "The amount of lives taken per kill has increased! You now take **" + livesPerKill + "** per kill!"));
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 4800, 1200);
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
        if (!auroraMCGamePlayer.isSpectator()) {
            for (Turret turret : new ArrayList<>(turrets.values())) {
                if (turret.getOwner().equals(auroraMCGamePlayer)) {
                    turret.getTask().cancel();
                    turret.getArmorStand().remove();
                    turrets.remove(turret.getArmorStand());
                }
            }
        }
        List<AuroraMCServerPlayer> blueAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && (player.getTeam() instanceof PBBlue) && !player.equals(auroraMCGamePlayer)).collect(Collectors.toList());
        List<AuroraMCServerPlayer> redAlive = ServerAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && (player.getTeam() instanceof PBRed) && !player.equals(auroraMCGamePlayer)).collect(Collectors.toList());
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

    public int getLivesPerKill() {
        return livesPerKill;
    }
}

/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run;

import net.auroramc.api.player.Team;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerDropItemEvent;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.*;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.run.kits.Berserker;
import net.auroramc.games.run.listeners.DeathListener;
import net.auroramc.games.run.listeners.LeapListener;
import net.auroramc.games.run.listeners.MoveListener;
import net.auroramc.games.run.util.RunScoreboardRunnable;
import net.auroramc.games.util.PlayersTeam;
import net.auroramc.games.util.listeners.death.NoDamageInstaKillListener;
import net.auroramc.games.util.listeners.settings.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Run extends Game {


    public Run(GameVariationInfo gameVariation) {
        super(gameVariation);
    }
    private DeathListener deathListener;
    private MoveListener moveListener;
    private RunScoreboardRunnable runnable;
    private LeapListener leapListener;


    @Override
    public void preLoad() {
        this.teams.put("players", new PlayersTeam());
        this.kits.add(new Berserker());
        runnable = new RunScoreboardRunnable();
        damagePvP = false;
        damagePvE = false;
        damageEvP = false;
        blockBreak = false;
        itemDrop = false;
        itemPickup = false;
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
            gp.getScoreboard().setTitle("&3&l-= &b&lRUN &3&l=-");
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
        deathListener = new DeathListener();
        moveListener = new MoveListener();
        leapListener = new LeapListener();
        DisableHungerListener.register();
        DisableBreakListener.register();
        DisablePlaceListener.register();
        NoDamageInstaKillListener.register();
        DisableItemDrop.register();
        DisableItemPickup.register();
        DisableMovableItems.register();
        DisableWeatherListener.register();
        Bukkit.getPluginManager().registerEvents(deathListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(moveListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(leapListener, EngineAPI.getGameEngine());
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
        PlayerShowEvent.getHandlerList().unregister(deathListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(deathListener);
        PlayerMoveEvent.getHandlerList().unregister(moveListener);
        PlayerInteractEvent.getHandlerList().unregister(leapListener);
        PlayerDropItemEvent.getHandlerList().unregister(leapListener);
        DisableHungerListener.unregister();
        DisableBreakListener.unregister();
        DisablePlaceListener.unregister();
        NoDamageInstaKillListener.unregister();
        DisableItemDrop.unregister();
        DisableItemPickup.unregister();
        DisableMovableItems.unregister();
        DisableWeatherListener.unregister();
        runnable.cancel();
    }

    @Override
    public void inProgress() {
        super.inProgress();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                    AuroraMCGamePlayer gp = (AuroraMCGamePlayer) pl;
                    if (gp.isSpectator() || gp.isVanished()) {
                        return;
                    }
                    Location location = gp.getLocation().clone();
                    location.setY(location.getY() - 1);
                    if (location.getBlock().getType() != Material.AIR && (location.getBlock().getType() != Material.STAINED_CLAY || location.getBlock().getData() != 14) && !location.getBlock().isLiquid()) {
                        gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                        location.getBlock().setType(Material.STAINED_CLAY);
                        location.getBlock().setData((byte) 14);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                location.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(ServerAPI.getCore(), 20);
                    }
                    int wholeX = (int) ((location.getX() >= 0)?Math.floor(location.getX()):Math.ceil(location.getX()));
                    int wholeZ = (int) ((location.getZ() >= 0)?Math.floor(location.getZ()):Math.ceil(location.getZ()));
                    double decX = Math.abs(location.getX() - wholeX);
                    double decZ = Math.abs(location.getZ() - wholeZ);
                    Location x = null;
                    Location z = null;
                    if (decX <= 0.31) {
                        x = location.clone();
                        if (location.getX() > 0) {
                            x.setX(x.getX() - 1);
                        } else {
                            x.setX(x.getX() + 1);
                        }
                    } else if (decX >= 0.69) {
                        x = location.clone();
                        if (location.getX() > 0) {
                            x.setX(x.getX() + 1);
                        } else {
                            x.setX(x.getX() - 1);
                        }
                    }
                    if (x != null && x.getBlock().getType() != Material.AIR && (x.getBlock().getType() != Material.STAINED_CLAY || x.getBlock().getData() != 14) && !x.getBlock().isLiquid()) {
                        gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                        x.getBlock().setType(Material.STAINED_CLAY);
                        x.getBlock().setData((byte)14);
                        Location finX = x;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                finX.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(ServerAPI.getCore(), 20);
                    }

                    if (decZ <= 0.31) {
                        z = location.clone();
                        if (z.getZ() > 0) {
                            z.setZ(z.getZ() - 1);
                        } else {
                            z.setZ(z.getZ() + 1);
                        }
                    } else if (decZ >= 0.69) {
                        z = location.clone();
                        if (z.getZ() > 0) {
                            z.setZ(z.getZ() + 1);
                        } else {
                            z.setZ(z.getZ() - 1);
                        }
                    }
                    if (z != null) {
                        if (z.getBlock().getType() != Material.AIR && (z.getBlock().getType() != Material.STAINED_CLAY || z.getBlock().getData() != 14) && !z.getBlock().isLiquid()) {
                            gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                            z.getBlock().setType(Material.STAINED_CLAY);
                            z.getBlock().setData((byte)14);
                            Location finZ = z;
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    finZ.getBlock().setType(Material.AIR);
                                }
                            }.runTaskLater(ServerAPI.getCore(), 20);
                        }
                        if (x != null) {
                            Location loc2 = x.clone();
                            loc2.setZ(z.getZ());
                            if (loc2.getBlock().getType() != Material.AIR && (loc2.getBlock().getType() != Material.STAINED_CLAY || loc2.getBlock().getData() != 14) && !loc2.getBlock().isLiquid()) {
                                gp.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "blocksBroken", 1, true);
                                loc2.getBlock().setType(Material.STAINED_CLAY);
                                loc2.getBlock().setData((byte)14);
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        loc2.getBlock().setType(Material.AIR);
                                    }
                                }.runTaskLater(ServerAPI.getCore(), 20);
                            }
                        }
                    }
                }
            }
        }.runTask(ServerAPI.getCore());
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

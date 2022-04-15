/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.tag;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.tag.kits.TagKit;
import net.auroramc.games.tag.listeners.HitListener;
import net.auroramc.games.tag.teams.RunnersTeam;
import net.auroramc.games.tag.teams.TaggedTeam;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tag extends Game {


    public Tag(GameVariation gameVariation) {
        super(gameVariation);
    }

    private TagScoreboardRunnable runnable;
    private HitListener hitListener;


    @Override
    public void preLoad() {
        this.teams.put("players", new RunnersTeam());
        this.kits.add(new TagKit());
        runnable = new TagScoreboardRunnable();
        hitListener = new HitListener();
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
            gp.getScoreboard().setTitle("&3&l-= &b&lTAG &3&l=-");
            if (gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y + 0.5, z, yaw, 0));
            } else {
                JSONObject spawn = spawns.getJSONObject(spawnIndex);
                int x, y, z;
                x = spawn.getInt("x");
                y = spawn.getInt("y");
                z = spawn.getInt("z");
                float yaw = spawn.getFloat("yaw");
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                spawnIndex++;
                if (spawnIndex >= spawns.length()) {
                    spawnIndex = 0;
                }
                gp.getKit().onGameStart(player);
            }
        }
        Bukkit.getPluginManager().registerEvents(hitListener, AuroraMCAPI.getCore());
        DisableBreakListener.register();
        DisableHungerListener.register();
        DisablePlaceListener.register();;
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
        TaggedTeam team = new TaggedTeam();
        this.teams.put("Tagged", team);
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator()).collect(Collectors.toList());
        int random = new Random().nextInt(playersAlive.size());
        AuroraMCPlayer player = playersAlive.get(random);
        player.setTeam(team);
        for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
            player1.updateNametag(player);
            if (player1.equals(player)) {
                if (player1.isDisguised()) {
                    if (player1.getPreferences().isHideDisguiseNameEnabled()) {
                        player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + player.getName() + "** was tagged by the game!"));
                        continue;
                    }
                }
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + player.getPlayer().getName() + "** was tagged by the game!"));
            } else {
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "**" + player.getPlayer().getName() + "** was tagged by the game!"));
            }
        }
    }

    private void end() {
        EntityDamageByEntityEvent.getHandlerList().unregister(hitListener);
        EntityDamageEvent.getHandlerList().unregister(hitListener);
        DisablePlaceListener.unregister();
        DisableBreakListener.unregister();
        DisableHungerListener.unregister();
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
        List<AuroraMCPlayer> playersAlive = AuroraMCAPI.getPlayers().stream().filter(player -> !((AuroraMCGamePlayer)player).isSpectator() && !(player.getTeam() instanceof TaggedTeam)).collect(Collectors.toList());
        if (playersAlive.size() == 1) {
            this.end(playersAlive.get(0));
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
}

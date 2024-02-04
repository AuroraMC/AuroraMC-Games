/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.run.variations.quick;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.hotpotato.variations.HotPotatoVariation;
import net.auroramc.games.run.Run;
import net.auroramc.games.run.variations.RunVariation;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Quick extends RunVariation {


    public Quick(Run game) {
        super(game);
    }


    @Override
    public boolean preLoad() {
        return false;
    }

    @Override
    public boolean load(GameMap gameMap) {
        return false;
    }

    @Override
    public boolean start() {
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Quick Run", "Welcome to Quick Run! When the game start, you will receive a huge speed boost. We advise you hold shift, or you death with be Quick! (haha get the joke xD)"));
        }
        return false;
    }

    @Override
    public void inProgress() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 49, true, false));
                }
            }
        }.runTask(EngineAPI.getGameEngine());
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public boolean onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {
        return false;
    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void onRespawn(AuroraMCGamePlayer player) {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 49, true, false));
                }
            }
        }.runTask(EngineAPI.getGameEngine());
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer player, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return true;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }


    @Override
    public void balanceTeams() {
        GameStartingRunnable.teamBalance();
    }
}

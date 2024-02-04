/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.spleef.variations.blind;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.run.Run;
import net.auroramc.games.run.variations.RunVariation;
import net.auroramc.games.spleef.Spleef;
import net.auroramc.games.spleef.variations.SpleefVariation;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Blind extends SpleefVariation {


    public Blind(Spleef game) {
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
        return false;
    }

    @Override
    public void inProgress() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 0, true, false));
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
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 0, true, false));
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

/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.persistence.Entity;

public class CrystalListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ENDER_CRYSTAL) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer(e.getPlayer());
            EnderCrystal crystal = (EnderCrystal) e.getRightClicked();
            if (!player.isSpectator()) {
                crystalCapture(player, crystal);
            }
        }
    }

    private void crystalCapture(AuroraMCGamePlayer player, EnderCrystal crystal) {
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        if (player.getTeam().getName().equalsIgnoreCase("Red")) {
            //Red
            //Check if its own crystal.
            if ((red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME && red.getBossCrystal().getCrystal().equals(crystal)) || (red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME && red.getTowerACrystal().getCrystal().equals(crystal)) || (red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME && red.getTowerBCrystal().getCrystal().equals(crystal))) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can't capture your own crystal!"));
                return;
            }

            if (!player.getGameData().containsKey("crystal_possession")) {
                if (blue.getBossCrystal().getCrystal().equals(crystal) && blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.DEAD && blue.getTowerACrystal().getState() == Crystal.CrystalState.DEAD) {
                        //Only capture if they're both dead.
                        Crystal crystal1 = blue.getBossCrystal();
                        crystal1.crystalCaptured(player, "BOSS");
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture a boss crystal if both tower crystals have been captured!"));
                    }
                } else if (blue.getTowerACrystal().getCrystal().equals(crystal) && blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = blue.getTowerACrystal();
                    if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "A");
                } else if (blue.getTowerBCrystal().getCrystal().equals(crystal) && blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = blue.getTowerBCrystal();
                    if (blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "B");
                } else {
                    //This is theoretically impossible. Ignore the event.
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You already have a crystal, return it first before capturing another!"));
            }
        } else {
            //Blue
            if ((blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME && blue.getBossCrystal().getCrystal().equals(crystal)) || (blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME && blue.getTowerACrystal().getCrystal().equals(crystal)) || (blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME && blue.getTowerBCrystal().getCrystal().equals(crystal))) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can't capture your own crystal!"));
                return;
            }

            if (!player.getGameData().containsKey("crystal_possession")) {
                if (red.getBossCrystal().getCrystal().equals(crystal) && red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    if (red.getTowerBCrystal().getState() == Crystal.CrystalState.DEAD && red.getTowerACrystal().getState() == Crystal.CrystalState.DEAD) {
                        //Only capture if they're both dead.
                        Crystal crystal1 = red.getBossCrystal();
                        crystal1.crystalCaptured(player, "BOSS");
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture a boss crystal if both tower crystals have been captured!"));
                    }
                } else if (red.getTowerACrystal().getCrystal().equals(crystal) && red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = red.getTowerACrystal();
                    if (red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "A");
                } else if (red.getTowerBCrystal().getCrystal().equals(crystal) && red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = red.getTowerBCrystal();
                    if (red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "B");
                } else {
                    //This is theoretically impossible. Ignore the event.
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You already have a crystal, return it first before capturing another!"));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof EnderCrystal) {
            e.setCancelled(true);
            if (e.getDamager() instanceof Player) {
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) e.getDamager());
                EnderCrystal crystal = (EnderCrystal) e.getEntity();
                if (!player.isSpectator()) {
                    crystalCapture(player, crystal);
                }
            }
        }
    }

}

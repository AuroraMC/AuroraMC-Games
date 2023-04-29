/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.events.entity.EntityDamageByPlayerEvent;
import net.auroramc.core.api.events.entity.PlayerDamageEvent;
import net.auroramc.core.api.events.player.PlayerInteractAtEntityEvent;
import net.auroramc.core.api.events.player.PlayerTeleportEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CrystalListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getClickedEntity().getType() == EntityType.ENDER_CRYSTAL) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            EnderCrystal crystal = (EnderCrystal) e.getClickedEntity();
            if (!player.isSpectator() && crystal.getCustomName() != null) {
                if (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp() - 10000 >= 120000L) {
                    crystalCapture(player, crystal);
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Game", "You cannot collect crystals within the first 2 minutes!"));
                }
            } else if (crystal.getCustomName() == null) {
                player.sendMessage(TextFormatter.pluginMessage("Game", "You can't collect a crystal that's already been captured!"));
            }
        }
    }

    private void crystalCapture(AuroraMCGamePlayer player, EnderCrystal crystal) {
        CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
        CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");

        if (player.getTeam().getName().equalsIgnoreCase("Red")) {
            //Red
            //Check if its own crystal.
            if ((red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("RedBoss")) || (red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("RedA")) || (red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("RedB"))) {
                player.sendMessage(TextFormatter.pluginMessage("Game", "You can't capture your own crystal!"));
                return;
            }

            if (!player.getGameData().containsKey("crystal_possession")) {
                if (crystal.getCustomName().equals("BlueBoss") && blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.DEAD && blue.getTowerACrystal().getState() == Crystal.CrystalState.DEAD) {
                        //Only capture if they're both dead.
                        Crystal crystal1 = blue.getBossCrystal();
                        crystal1.crystalCaptured(player, "BOSS");
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture a boss crystal if both tower crystals have been captured!"));
                    }
                } else if (crystal.getCustomName().equals("BlueA") && blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = blue.getTowerACrystal();
                    if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "A");
                } else if (crystal.getCustomName().equals("BlueB") && blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = blue.getTowerBCrystal();
                    if (blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "B");
                } else {
                    //This is theoretically impossible. Ignore the event.
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Game", "You already have a crystal, return it first before capturing another!"));
            }
        } else {
            //Blue
            if ((blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("BlueBoss")) || (blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("BlueA")) || (blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME && crystal.getCustomName().equals("BlueB"))) {
                player.sendMessage(TextFormatter.pluginMessage("Game", "You can't capture your own crystal!"));
                return;
            }

            if (!player.getGameData().containsKey("crystal_possession")) {
                if (crystal.getCustomName().equals("RedBoss") && red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    if (red.getTowerBCrystal().getState() == Crystal.CrystalState.DEAD && red.getTowerACrystal().getState() == Crystal.CrystalState.DEAD) {
                        //Only capture if they're both dead.
                        Crystal crystal1 = red.getBossCrystal();
                        crystal1.crystalCaptured(player, "BOSS");
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture a boss crystal if both tower crystals have been captured!"));
                    }
                } else if (crystal.getCustomName().equals("RedA") && red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = red.getTowerACrystal();
                    if (red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "A");
                } else if (crystal.getCustomName().equals("RedB") && red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                    Crystal crystal1 = red.getTowerBCrystal();
                    if (red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You can only capture one crystal at any one time! Wait till your currently captured crystal has been returned!"));
                        return;
                    }
                    crystal1.crystalCaptured(player, "B");
                } else {
                    //This is theoretically impossible. Ignore the event.
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Game", "You already have a crystal, return it first before capturing another!"));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByPlayerEvent e) {
        if (e.getDamaged() instanceof EnderCrystal) {
            e.setCancelled(true);
            if (e.getCause() == PlayerDamageEvent.DamageCause.ENTITY_ATTACK) {
                AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
                EnderCrystal crystal = (EnderCrystal) e.getDamaged();
                if (!player.isSpectator() && crystal.getCustomName() != null) {
                    if (System.currentTimeMillis() - EngineAPI.getActiveGame().getGameSession().getStartTimestamp() - 10000 >= 120000L) {
                        crystalCapture(player, crystal);
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "You cannot collect crystals within the first 2 minutes!"));
                    }
                } else if (crystal.getCustomName() == null) {
                    player.sendMessage(TextFormatter.pluginMessage("Game", "You can't collect a crystal that's already been captured!"));
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
            if (player.getGameData().containsKey("crystal_possession")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Game", "You cannot use ender pearls while you have a crystal!"));
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
            }
        }
    }

}

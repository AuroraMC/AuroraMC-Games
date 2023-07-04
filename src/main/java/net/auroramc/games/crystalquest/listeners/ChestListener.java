/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.JSONObject;

public class ChestListener implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        AuroraMCGamePlayer player = (AuroraMCGamePlayer) e.getPlayer();
        if (e.getClickedBlock() != null && !player.isSpectator()) {
            if (e.getClickedBlock().getType() == Material.CHEST) {
                if (player.getTeam() instanceof CQBlue) {
                    CQRed red = (CQRed) EngineAPI.getActiveGame().getTeams().get("Red");
                    if (!((CQBlue) player.getTeam()).getChest().equals(e.getClickedBlock()) && red.getBossCrystal().getState() != Crystal.CrystalState.DEAD) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your teams chest!"));
                        e.setCancelled(true);
                    } else {
                        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Team " + player.getTeam().getName() + " Chest Opened").put("player", player.getByDisguiseName())));
                    }
                } else {
                    CQBlue blue = (CQBlue) EngineAPI.getActiveGame().getTeams().get("Blue");
                    if (!((CQRed) player.getTeam()).getChest().equals(e.getClickedBlock()) && blue.getBossCrystal().getState() != Crystal.CrystalState.DEAD) {
                        player.sendMessage(TextFormatter.pluginMessage("Game", "This is not your teams chest!"));
                        e.setCancelled(true);
                    } else {
                        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Team " + player.getTeam().getName() + " Chest Opened").put("player", player.getByDisguiseName())));
                    }
                }
            }
        }
    }

}

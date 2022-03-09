/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.listeners;

import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.LookupOp;

public class CrystalReturnListener implements Listener {

    private final Crystal crystal;

    public CrystalReturnListener(Crystal crystal) {
        this.crystal = crystal;
    }

    @EventHandler
    public void onReturn(PlayerMoveEvent e) {
        if (e.getPlayer().equals(crystal.getHolder().getPlayer())) {
            if (!e.getTo().getBlock().equals(e.getFrom().getBlock())) {
                JSONArray returnPoints = EngineAPI.getActiveMap().getMapData().getJSONObject("game").getJSONObject("CRYSTAL").getJSONArray("RETURN " + ((crystal.getHomeTeam().getName().equalsIgnoreCase("Red")?"BLUE":"RED")));
                JSONObject pointA = returnPoints.getJSONObject(0);
                JSONObject pointB = returnPoints.getJSONObject(1);
                JSONObject pointC = returnPoints.getJSONObject(2);
                Location a = new Location(EngineAPI.getMapWorld(), pointA.getInt("x"), pointA.getInt("y"), pointA.getInt("z"));
                Location b = new Location(EngineAPI.getMapWorld(), pointB.getInt("x"), pointB.getInt("y"), pointB.getInt("z"));
                Location c = new Location(EngineAPI.getMapWorld(), pointC.getInt("x"), pointC.getInt("y"), pointC.getInt("z"));
                if (e.getTo().distanceSquared(a) < 25 || e.getTo().distanceSquared(b) < 25 || e.getTo().distanceSquared(c) < 25) {
                    if (crystal.isBoss()) {
                        //Boss Crystal. Use point C.
                        crystal.crystalDead(c);
                    } else {
                        if (crystal.getHomeTeam() instanceof CQRed) {
                            CQRed red = (CQRed) crystal.getHomeTeam();
                            if (red.getTowerACrystal().equals(crystal)) {
                                //Tower A Crystal. Use point A.
                                crystal.crystalDead(a);
                            } else {
                                //Tower B Crystal. Use point B.
                                crystal.crystalDead(b);
                            }
                        } else {
                            CQBlue blue = (CQBlue) crystal.getHomeTeam();
                            if (blue.getTowerACrystal().equals(crystal)) {
                                //Tower A Crystal. Use point A.
                                crystal.crystalDead(a);
                            } else {
                                //Tower B Crystal. Use point B.
                                crystal.crystalDead(b);
                            }
                        }
                    }
                }
            }
        }
    }

}

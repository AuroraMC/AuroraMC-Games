/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerShow(PlayerShowEvent e) {
        if (EngineAPI.getServerState() == ServerState.IN_GAME) {
            e.setHidden(true);
        }
    }
}

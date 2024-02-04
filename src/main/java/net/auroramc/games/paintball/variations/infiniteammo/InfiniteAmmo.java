/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.variations.infiniteammo;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.hotpotato.variations.HotPotatoVariation;
import net.auroramc.games.paintball.Paintball;
import net.auroramc.games.paintball.variations.PaintballVariation;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfiniteAmmo extends PaintballVariation {

    public InfiniteAmmo(Paintball game) {
        super(game);
    }

    @Override
    public void onThrow(AuroraMCGamePlayer player) {
        player.getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, 64, null).getItemStack());
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

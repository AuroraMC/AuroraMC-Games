/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CrystalQuest extends Game {

    public static final ItemStack compass;

    static {
        compass = new GUIItem(Material.COMPASS, "&3Crystal Compass", 1, ";&rThe compass will display the distance;&rfrom the closest crystal currently captured.").getItem();
    }


    public CrystalQuest(GameVariation gameVariation) {
        super(gameVariation);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load(GameMap gameMap) {

    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void generateTeam(AuroraMCPlayer auroraMCPlayer) {

    }

    @Override
    public void onPlayerJoin(Player player) {

    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public List<Kit> getKits() {
        return new ArrayList<>();
    }
}

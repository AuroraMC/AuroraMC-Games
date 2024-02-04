/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.variations.yolt;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import net.auroramc.games.hotpotato.HotPotato;
import net.auroramc.games.hotpotato.entities.Potato;
import net.auroramc.games.hotpotato.variations.HotPotatoVariation;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YOLT extends HotPotatoVariation {

    private int lives;
    private final Map<UUID, Integer> livesLost;

    public YOLT(HotPotato game) {
        super(game);
        lives = 3;
        livesLost = new HashMap<>();
    }

    @Override
    public int onGeneratePotatoes(int potatoes, int playersAlive) {
        return potatoes;
    }

    @Override
    public boolean onExplode(Potato potato) {
        if (livesLost.containsKey(potato.getHolder().getUniqueId())) {
            int i = livesLost.get(potato.getHolder().getUniqueId());
            if (++i >= lives) {
                potato.getHolder().sendMessage(TextFormatter.pluginMessage("YOLT", "You ran out of lives, so you are now permanently dead."));
                return true;
            } else {
                livesLost.put(potato.getHolder().getUniqueId(), i);
                potato.getHolder().sendMessage(TextFormatter.pluginMessage("YOLT", "You have **" + (lives - i) + "** lives left."));
            }
        } else {
            livesLost.put(potato.getHolder().getUniqueId(), 1);
            potato.getHolder().sendMessage(TextFormatter.pluginMessage("YOLT", "You have **" + (lives - 1) + "** lives left."));
        }

        return false;
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
            player.sendMessage(TextFormatter.pluginMessage("YOLO", "You are playing the YOLT (You Only Live Twice) game variation! You have more lives, and can endure **" + lives + "** potato explosions!"));
        }
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

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }


    @Override
    public void balanceTeams() {
        GameStartingRunnable.teamBalance();
    }
}

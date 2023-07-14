/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.yolo;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YOLO extends CrystalQuestVariation {

    private int lives;
    private final Map<UUID, Integer> livesLost;

    public YOLO(CrystalQuest game) {
        super(game);
        lives = 5;
        livesLost = new HashMap<>();
    }

    @Override
    public void onCrystalDestroy() {
    }

    @Override
    public void onMineGenerate() {

    }

    @Override
    public void onCrystalCaptured(Crystal crystal) {

    }

    @Override
    public void onCrystalDead(Crystal crystal) {

    }

    @Override
    public void onCrystalReturned(Crystal crystal) {

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
        if (livesLost.containsKey(player.getUniqueId())) {
            int i = livesLost.get(player.getUniqueId());
            if (i++ >= lives) {
                player.sendMessage(TextFormatter.pluginMessage("YOLO", "You ran out of lives, so you are now permanently dead."));
                return true;
            } else {
                livesLost.put(player.getUniqueId(), i);
                player.sendMessage(TextFormatter.pluginMessage("YOLO", "You have **" + (lives - i) + "** lives left."));
            }
        } else {
            livesLost.put(player.getUniqueId(), 1);
            player.sendMessage(TextFormatter.pluginMessage("YOLO", "You have **" + (lives - 1) + "** lives left."));
        }

        return false;
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
    public boolean onShopPurchase(Material material) {
        return true;
    }

    @Override
    public GUIItem onShopDisplayItem(GUIItem material) {
        return material;
    }


    @Override
    public void balanceTeams() {
        GameStartingRunnable.teamBalance();
    }
}

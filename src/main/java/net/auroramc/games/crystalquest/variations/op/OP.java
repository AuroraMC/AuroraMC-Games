/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.op;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class OP extends CrystalQuestVariation {

    public OP(CrystalQuest game) {
        super(game);
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
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            if (!gp.isSpectator()) {
                player.getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
                player.getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
                player.getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
                player.getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);

                int swordSlot = 0, pickSlot = 1, axeSlot = 2;

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                        swordSlot = i;
                    } else if (player.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                        axeSlot = i;
                    } else if (player.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                        pickSlot = i;
                    }
                }

                ItemStack stack = player.getInventory().getItem(swordSlot);
                stack.setType(Material.DIAMOND_SWORD);
                stack = player.getInventory().getItem(axeSlot);
                stack.setType(Material.DIAMOND_AXE);
                stack = player.getInventory().getItem(pickSlot);
                stack.setType(Material.DIAMOND_PICKAXE);
            }
        }
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
        player.getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
        player.getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
        player.getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
        player.getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);

        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (player.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }

        ItemStack stack = player.getInventory().getItem(swordSlot);
        stack.setType(Material.DIAMOND_SWORD);
        stack = player.getInventory().getItem(axeSlot);
        stack.setType(Material.DIAMOND_AXE);
        stack = player.getInventory().getItem(pickSlot);
        stack.setType(Material.DIAMOND_PICKAXE);
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

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

/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.knockback;

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

public class Knockback extends CrystalQuestVariation {

    public Knockback(CrystalQuest game) {
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
    public boolean onShopPurchase(Material material) {
        return true;
    }

    @Override
    public GUIItem onShopDisplayItem(GUIItem material) {
        return material;
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
        int cookieSlot = -1;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                continue;
            }
            if (player.getInventory().getItem(i).getType().name().equals("COOKIE")) {
                cookieSlot = i;
                break;
            }
        }

        if (cookieSlot == -1) {
            ItemStack stack = new ItemStack(Material.COOKIE);
            stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 200);
            ItemMeta itemMeta = stack.getItemMeta();
            itemMeta.setDisplayName("§cMmmmmm, cookie.");
            stack.setItemMeta(itemMeta);
            player.getInventory().addItem(stack);
        }
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void balanceTeams() {
        GameStartingRunnable.teamBalance();
    }
}
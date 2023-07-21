/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.variations.hungergames;

import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.kits.*;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import net.auroramc.games.crystalquest.variations.hungergames.kits.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HungerGames extends CrystalQuestVariation {

    public HungerGames(CrystalQuest game) {
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
        return !material.name().endsWith("_SWORD");
    }

    @Override
    public GUIItem onShopDisplayItem(GUIItem material) {
        if (material.getItemStack().getType().name().endsWith("_SWORD")) {
            return new GUIItem(Material.BARRIER, "&3Sword Upgrade", 1, ";&r&fPurchasing Swords is disabled in this game variation.");
        }
        return material;
    }

    @Override
    public boolean preLoad() {
        getGame().getKits().clear();
        getGame().getKits().add(new HGMiner());
        getGame().getKits().add(new HGDefender());
        getGame().getKits().add(new HGFighter());
        getGame().getKits().add(new HGArcher());
        getGame().getKits().add(new HGEconomist());
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
        if (!(player.getKit() instanceof Archer)) {
            player.getInventory().setItem(0, new GUIItem(Material.BOW).getItemStack());
            player.getInventory().setItem(8, new GUIItem(Material.ARROW, null, 64).getItemStack());
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

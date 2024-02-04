/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.oresbegone;

import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.engine.api.util.GameStartingRunnable;
import net.auroramc.games.crystalquest.CrystalQuest;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OresBeGone extends CrystalQuestVariation {

    public OresBeGone(CrystalQuest game) {
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
        this.getGame().setMineMultiplier(0f);
        return false;
    }

    @Override
    public boolean load(GameMap gameMap) {
        return false;
    }

    @Override
    public boolean start() {
        ((CQRed)this.getGame().getTeams().get("Red")).newRobot().spawn();
        ((CQRed)this.getGame().getTeams().get("Red")).newRobot().spawn();
        ((CQBlue)this.getGame().getTeams().get("Blue")).newRobot().spawn();
        ((CQBlue)this.getGame().getTeams().get("Blue")).newRobot().spawn();
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

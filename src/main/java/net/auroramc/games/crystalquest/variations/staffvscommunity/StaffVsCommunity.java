/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.staffvscommunity;

import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
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

public class StaffVsCommunity extends CrystalQuestVariation {

    public StaffVsCommunity(CrystalQuest game) {
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
        this.getGame().setCustomTeamBalancing(true);
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
    public boolean onDeath(AuroraMCGamePlayer auroraMCGamePlayer, AuroraMCGamePlayer auroraMCGamePlayer1) {
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {

    }

    @Override
    public void balanceTeams() {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player1;
            if (!gp.isSpectator()) {
                if (gp.hasPermission("moderation") || gp.hasPermission("event.host")) {
                    gp.setTeam(this.getGame().getTeams().get("Red"));

                } else {
                    gp.setTeam(this.getGame().getTeams().get("Blue"));
                }
                gp.getTeam().getPlayers().add(gp);
                gp.sendMessage(TextFormatter.pluginMessage("Game Manager", String.format("You have been assigned to the %s%s§r team", gp.getTeam().getTeamColor(), gp.getTeam().getName())));
                for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                    pl.updateNametag(gp);
                }
            }
            if (gp.getKit() == null && !gp.isSpectator()) {
                gp.setKit(EngineAPI.getActiveGame().getKits().get(0));
            }
        }
    }
}

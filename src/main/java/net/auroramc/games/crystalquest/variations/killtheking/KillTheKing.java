/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.variations.killtheking;

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
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.UUID;

public class KillTheKing extends CrystalQuestVariation {

    AuroraMCGamePlayer kingBlue, kingRed;

    public KillTheKing(CrystalQuest game) {
        super(game);
        kingBlue = kingRed = null;
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
        return !material.name().contains("DIAMOND");
    }

    @Override
    public GUIItem onShopDisplayItem(GUIItem material) {
        if (material.getItemStack().getType().name().contains("DIAMOND")) {
            return new GUIItem(Material.BARRIER, material.getItemStack().getItemMeta().getDisplayName(), material.getItemStack().getAmount(), ";&r&fFurther upgrades in this game variation are disabled.");
        }
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

        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Kill The King", "You are playing the Kill The King game variation! The kings are:\n" +
                    "Red: **" + kingRed.getByDisguiseName() + "**\n" +
                    "Blue: **" + kingBlue.getByDisguiseName() + "**\n\n" +
                    "Most gameplay is normal but if your king dies, it is game over and the other team wins! Protect your king at all costs!\n" +
                    "All Diamond upgrades are disabled in the shop, and the King is in permanent full Diamond. All other gameplay is normal."));
        }
        return false;
    }

    @Override
    public void inProgress() {
        kingRed.getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
        kingRed.getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
        kingRed.getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
        kingRed.getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);

        int swordSlot = 0, pickSlot = 1, axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (kingRed.getInventory().getItem(i) == null) {
                continue;
            }
            if (kingRed.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (kingRed.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (kingRed.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }

        ItemStack stack = kingRed.getInventory().getItem(swordSlot);
        stack.setType(Material.DIAMOND_SWORD);
        stack = kingRed.getInventory().getItem(axeSlot);
        stack.setType(Material.DIAMOND_AXE);
        stack = kingRed.getInventory().getItem(pickSlot);
        stack.setType(Material.DIAMOND_PICKAXE);

        kingBlue.getInventory().getHelmet().setType(Material.DIAMOND_HELMET);
        kingBlue.getInventory().getBoots().setType(Material.DIAMOND_BOOTS);
        kingBlue.getInventory().getChestplate().setType(Material.DIAMOND_CHESTPLATE);
        kingBlue.getInventory().getLeggings().setType(Material.DIAMOND_LEGGINGS);

        swordSlot = 0;
        pickSlot = 1;
        axeSlot = 2;

        for (int i = 0; i < 36; i++) {
            if (kingBlue.getInventory().getItem(i) == null) {
                continue;
            }
            if (kingBlue.getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                swordSlot = i;
            } else if (kingBlue.getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                axeSlot = i;
            } else if (kingBlue.getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                pickSlot = i;
            }
        }

        stack = kingBlue.getInventory().getItem(swordSlot);
        stack.setType(Material.DIAMOND_SWORD);
        stack = kingBlue.getInventory().getItem(axeSlot);
        stack.setType(Material.DIAMOND_AXE);
        stack = kingBlue.getInventory().getItem(pickSlot);
        stack.setType(Material.DIAMOND_PICKAXE);
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
        if (auroraMCGamePlayer.equals(kingBlue) || auroraMCGamePlayer.equals(kingRed)) {
            return true;
        }
        return false;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer auroraMCGamePlayer) {
        if (auroraMCGamePlayer.equals(kingBlue)) {
            EngineAPI.getActiveGame().end(EngineAPI.getActiveGame().getTeams().get("Red"), null);
        } else if (auroraMCGamePlayer.equals(kingRed)) {
            EngineAPI.getActiveGame().end(EngineAPI.getActiveGame().getTeams().get("Blue"), null);
        }
    }

    @Override
    public void balanceTeams() {
        if (kingBlue != null && !kingBlue.isOptedSpec()) {
            kingBlue.setTeam(EngineAPI.getActiveGame().getTeams().get("Blue"));
            kingBlue.getTeam().getPlayers().add(kingBlue);
            for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                pl.updateNametag(kingBlue);
            }
        }
        if (kingRed != null && !kingRed.isOptedSpec()) {
            kingRed.setTeam(EngineAPI.getActiveGame().getTeams().get("Red"));
            kingRed.getTeam().getPlayers().add(kingRed);
            for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                pl.updateNametag(kingRed);
            }
        }

        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player1;
            if (player1.getTeam() == null && !gp.isSpectator()) {
                Team leastPlayers = null;
                for (Team team : EngineAPI.getActiveGame().getTeams().values()) {
                    if (leastPlayers == null) {
                        leastPlayers = team;
                        continue;
                    }
                    if (leastPlayers.getPlayers().size() > team.getPlayers().size()) {
                        leastPlayers = team;
                    }
                }
                if (leastPlayers != null) {
                    leastPlayers.getPlayers().add(player1);
                    player1.setTeam(leastPlayers);
                    player1.sendMessage(TextFormatter.pluginMessage("Game Manager", String.format("You have been assigned to the %s%s§r team", leastPlayers.getTeamColor(), leastPlayers.getName())));
                    for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                        pl.updateNametag(player1);
                    }
                }
            }
            if (gp.getKit() == null && !gp.isSpectator()) {
                gp.setKit(EngineAPI.getActiveGame().getKits().get(0));
            }
        }
        if (kingBlue == null || kingBlue.isOptedSpec()) {
            Team team = EngineAPI.getActiveGame().getTeams().get("Blue");
            kingBlue = (AuroraMCGamePlayer) team.getPlayers().get(new Random().nextInt(team.getPlayers().size()));
            kingBlue.sendMessage(TextFormatter.pluginMessage("Kill The King", "You have been selected as the **Blue** teams king!"));
        }
        if (kingRed == null || kingRed.isOptedSpec()) {
            Team team = EngineAPI.getActiveGame().getTeams().get("Red");
            kingRed = (AuroraMCGamePlayer) team.getPlayers().get(new Random().nextInt(team.getPlayers().size()));
            kingRed.sendMessage(TextFormatter.pluginMessage("Kill The King", "You have been selected as the **Red** teams king!"));
        }
    }

    public AuroraMCGamePlayer getKingBlue() {
        return kingBlue;
    }

    public AuroraMCGamePlayer getKingRed() {
        return kingRed;
    }

    public void setKingBlue(AuroraMCGamePlayer kingBlue) {
        this.kingBlue = kingBlue;
    }

    public void setKingRed(AuroraMCGamePlayer kingRed) {
        this.kingRed = kingRed;
    }
}

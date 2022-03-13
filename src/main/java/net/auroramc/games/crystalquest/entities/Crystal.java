/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.listeners.CrystalReturnListener;
import net.auroramc.games.crystalquest.teams.CQBlue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Crystal {

    private EnderCrystal crystal;
    private final Location home;
    private CrystalState state;
    private AuroraMCGamePlayer holder;
    private final boolean boss;
    private final Team homeTeam;
    private CrystalReturnListener listener;

    public Crystal(Location location, Team homeTeam, boolean boss, String type) {
        this.home = location;
        this.state = CrystalState.AT_HOME;
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
        crystal.setCustomName(homeTeam.getName() + type);
        this.boss = boss;
        this.homeTeam = homeTeam;
        listener = new CrystalReturnListener(this);
    }

    public void crystalCaptured(AuroraMCGamePlayer holder, String type) {
        if (this.holder != null) {
            return;
        }
        this.holder = holder;
        crystal.remove();
        crystal = null;
        state = CrystalState.CAPTURED;
        listener = new CrystalReturnListener(this);
        Bukkit.getPluginManager().registerEvents(listener, EngineAPI.getGameEngine());
        holder.getGameData().put("crystal_possession", type);
        holder.getGameData().put("crystal_inventory", holder.getPlayer().getInventory().getContents());
        holder.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1, true, false));
        holder.getPlayer().getInventory().clear();

        for (int i = 0;i < 36;i++) {
            holder.getPlayer().getInventory().setItem(i, new GUIItem(Material.NETHER_STAR, "&3&lCollected Crystal", 1, ";&rReturn this to your base!").getItem());
        }

        String team = "&c&l";
        if (homeTeam instanceof CQBlue) {
            team = "&9&l";
        }
        String finalMessage = team + holder.getPlayer().getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            player.sendTitle(AuroraMCAPI.getFormatter().convert(team + homeTeam.getName() + ((isBoss())?" Boss Crystal Collected!":" Tower Crystal Collected!")), holder.getPlayer().getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"), 20, 100, 20, ChatColor.BLUE, ChatColor.RESET, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", finalMessage + " &r" + ((player.getTeam().equals(homeTeam)?"Kill them to return it to the base!":"Protect them at all costs."))));
        }
    }

    public void crystalDead(Location location) {
        this.state = CrystalState.DEAD;
        this.holder.getPlayer().getInventory().setContents((ItemStack[]) this.holder.getGameData().remove("crystal_inventory"));
        this.holder.getGameData().remove("crystal_possession");
        this.holder.getPlayer().removePotionEffect(PotionEffectType.SLOW);

        this.holder.getRewards().addXp("Crystal Capture", 50);
        this.holder.getStats().incrementStatistic(1, "crystalsCaptured", 1, true);

        home.getWorld().createExplosion(home, 6 * (isBoss()?2:1));

        String team = "&c&l";
        if (homeTeam instanceof CQBlue) {
            team = "&9&l";
        }
        String finalMessage = team + holder.getPlayer().getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            player.sendTitle(AuroraMCAPI.getFormatter().convert(team + homeTeam.getName() + ((isBoss())?" Boss Crystal Captured!":" Tower Crystal Captured!")), holder.getPlayer().getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"), 20, 100, 20, ChatColor.BLUE, ChatColor.RESET, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", finalMessage + " &r" + ((isBoss())?homeTeam.getName() + " can no longer respawn!":"")));
        }
        this.holder = null;

        unregisterListener();
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
    }

    public void crystalReturned() {
        this.holder.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        this.holder = null;
        crystal = EngineAPI.getMapWorld().spawn(home, EnderCrystal.class);
        state = CrystalState.AT_HOME;

        String team = "&c&l";
        if (homeTeam instanceof CQBlue) {
            team = "&9&l";
        }
        String finalMessage = team + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal Returned!":"Tower Crystal Returned!");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            player.sendTitle(AuroraMCAPI.getFormatter().convert(finalMessage), "", 20, 100, 20, ChatColor.BLUE, ChatColor.RESET, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", finalMessage + ""));
        }
        unregisterListener();
    }

    public void unregisterListener() {
        if (listener != null) {
            PlayerMoveEvent.getHandlerList().unregister(this.listener);
            this.listener = null;
        }
    }

    public EnderCrystal getCrystal() {
        return crystal;
    }

    public AuroraMCGamePlayer getHolder() {
        return holder;
    }


    public Location getHome() {
        return home;
    }

    public CrystalState getState() {
        return state;
    }

    public boolean isBoss() {
        return boss;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public enum CrystalState {AT_HOME, CAPTURED, DEAD}

}

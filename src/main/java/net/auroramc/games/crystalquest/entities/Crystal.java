/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.core.api.players.Team;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.listeners.CrystalReturnListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

    public Crystal(Location location, Team homeTeam, boolean boss) {
        this.home = location;
        this.state = CrystalState.AT_HOME;
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
        this.boss = boss;
        this.homeTeam = homeTeam;
        listener = new CrystalReturnListener(this);
    }

    public void crystalCaptured(AuroraMCGamePlayer holder, String type) {
        this.holder = holder;
        crystal.remove();
        crystal = null;
        state = CrystalState.CAPTURED;
        listener = new CrystalReturnListener(this);
        Bukkit.getPluginManager().registerEvents(listener, EngineAPI.getGameEngine());
        holder.getGameData().put("crystal_possession", type);
        holder.getGameData().put("crystal_inventory", holder.getPlayer().getInventory().getContents());
        holder.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 0, true, false));
        holder.getPlayer().getInventory().clear();
        for (int i = 0;i < 36;i++) {
            holder.getPlayer().getInventory().setItem(i, new GUIItem(Material.NETHER_STAR, "&3&lCaptured Crystal", 1, ";&rReturn this to your base!").getItem());
        }
    }

    public void crystalDead(Location location) {
        this.state = CrystalState.DEAD;
        this.holder = null;
        unregisterListener();
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
    }

    public void crystalReturned() {
        this.holder = null;
        crystal = EngineAPI.getMapWorld().spawn(home, EnderCrystal.class);
        state = CrystalState.AT_HOME;
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

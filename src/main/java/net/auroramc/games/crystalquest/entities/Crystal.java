/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.listeners.CrystalReturnListener;
import net.auroramc.games.crystalquest.teams.CQBlue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONObject;

public class Crystal {

    private EnderCrystal crystal;
    private final Location home;
    private CrystalState state;
    private AuroraMCGamePlayer holder;
    private final boolean boss;
    private final Team homeTeam;
    private CrystalReturnListener listener;
    private final String type;

    public Crystal(Location location, Team homeTeam, boolean boss, String type) {
        this.home = location;
        this.state = CrystalState.AT_HOME;
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
        this.type = type;
        crystal.setCustomName(homeTeam.getName() + type);
        this.boss = boss;
        this.homeTeam = homeTeam;
        listener = new CrystalReturnListener(this);
    }

    public void crystalCaptured(AuroraMCGamePlayer holder, String type) {
        if (this.holder != null) {
            return;
        }

        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Collected").put("player", holder.getPlayer().getName())));
        this.holder = holder;
        this.holder.getStats().incrementStatistic(1, "crystalsCollected", 1, true);
        crystal.remove();
        crystal = null;
        state = CrystalState.CAPTURED;
        listener = new CrystalReturnListener(this);
        Bukkit.getPluginManager().registerEvents(listener, EngineAPI.getGameEngine());
        holder.getGameData().put("crystal_possession", type);
        holder.getGameData().put("crystal_inventory", holder.getPlayer().getInventory().getContents());
        holder.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 0, true, false));
        holder.getPlayer().getInventory().clear();
        this.holder.getPlayer().setFoodLevel(3);

        ItemStack stack = new GUIItem(Material.NETHER_STAR, "&3&lCollected Crystal", 1, ";&rReturn this to your base!").getItem();
        stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);

        for (int i = 0;i < 36;i++) {
            holder.getPlayer().getInventory().setItem(i, stack);
        }

        String team = "&c&l";
        if (homeTeam instanceof CQBlue) {
            team = "&9&l";
        }
        String finalMessage = team + holder.getPlayer().getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            player.sendTitle(AuroraMCAPI.getFormatter().convert(team + homeTeam.getName() + ((isBoss())?" Boss Crystal Collected!":" Tower Crystal Collected!")), holder.getPlayer().getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"), 20, 100, 20, ChatColor.BLUE, ChatColor.RESET, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", finalMessage + " &r" + ((player.getTeam() != null)?((player.getTeam().equals(homeTeam)?"Kill them to return it to the base!":"Protect them at all costs.")):"")));
        }
    }

    public void crystalDead(Location location, boolean message) {
        this.state = CrystalState.DEAD;
        if (this.holder != null) {
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Captured").put("player", holder.getPlayer().getName())));
            this.holder.getStats().addProgress(AuroraMCAPI.getAchievement(62), 1, this.holder.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(62), 0), true);
            this.holder.getPlayer().getInventory().setContents((ItemStack[]) this.holder.getGameData().remove("crystal_inventory"));
            this.holder.getGameData().remove("crystal_possession");
            this.holder.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            this.holder.getPlayer().setFoodLevel(25);

            if (message) {
                this.holder.getRewards().addXp("Crystal Capture", 50);
                this.holder.getStats().incrementStatistic(1, "crystalsCaptured", 1, true);
                String team = "&c&l";
                if (homeTeam instanceof CQBlue) {
                    team = "&9&l";
                }
                String finalMessage = team + holder.getPlayer().getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    player.sendTitle(AuroraMCAPI.getFormatter().convert(team + homeTeam.getName() + ((isBoss())?" Boss Crystal Captured!":" Tower Crystal Captured!")), holder.getPlayer().getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"), 20, 100, 20, ChatColor.BLUE, ChatColor.RESET, true, false);
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", finalMessage + " &r" + ((isBoss())?homeTeam.getName() + " can no longer respawn!":"")));
                }
            }
            this.holder = null;
        } else {
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Destroyed By The Game")));
        }

        home.getWorld().createExplosion(home, 6);



        unregisterListener();
        crystal = EngineAPI.getMapWorld().spawn(location, EnderCrystal.class);
    }


    public void crystalReturned() {
        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Returned").put("player", holder.getPlayer().getName())));
        this.holder.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
        this.holder.getPlayer().setFoodLevel(25);
        if (!this.holder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(64))) {
            this.holder.getStats().achievementGained(AuroraMCAPI.getAchievement(64), 1, true);
        }
        this.holder = null;
        crystal = EngineAPI.getMapWorld().spawn(home, EnderCrystal.class);
        crystal.setCustomName(homeTeam.getName() + type);
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

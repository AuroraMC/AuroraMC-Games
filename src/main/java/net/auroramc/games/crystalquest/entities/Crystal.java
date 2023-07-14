/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.crystalquest.entities;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.listeners.CrystalReturnListener;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.variations.CrystalQuestVariation;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderCrystal;
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

        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Collected").put("player", holder.getByDisguiseName())));
        this.holder = holder;
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onCrystalCaptured(this);
        }
        this.holder.getStats().incrementStatistic(1, "crystalsCollected", 1, true);
        crystal.remove();
        crystal = null;
        state = CrystalState.CAPTURED;
        listener = new CrystalReturnListener(this);
        Bukkit.getPluginManager().registerEvents(listener, EngineAPI.getGameEngine());
        holder.getGameData().put("crystal_possession", type);
        holder.getGameData().put("crystal_inventory", holder.getInventory().getContents());
        holder.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 0, true, false));
        holder.getInventory().clear();
        this.holder.setFoodLevel(3);

        ItemStack stack = new GUIItem(Material.NETHER_STAR, "&3&lCollected Crystal", 1, ";&rReturn this to your base!").getItemStack();
        stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);

        for (int i = 0;i < 36;i++) {
            holder.getInventory().setItem(i, stack);
        }

        ChatColor team = ChatColor.RED;
        if (homeTeam instanceof CQBlue) {
            team = ChatColor.BLUE;
        }
        String finalMessage = team + "§l" + holder.getByDisguiseName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");

        TextComponent title = new TextComponent(homeTeam.getName() + ((isBoss())?" Boss Crystal Collected!":" Tower Crystal Collected!"));
        title.setColor(team);
        title.setBold(true);

        TextComponent subtitle = new TextComponent(holder.getByDisguiseName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"));
        subtitle.setBold(false);
        subtitle.setColor(ChatColor.WHITE);

        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {

            player.sendTitle(title, ((player.equals(holder) && player.isDisguised() && player.getPreferences().isHideDisguiseNameEnabled())?new TextComponent(holder.getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!")):subtitle), 20, 100, 20);
            player.sendMessage(TextFormatter.pluginMessage("Game", ((player.equals(holder) && player.isDisguised() && player.getPreferences().isHideDisguiseNameEnabled())?team + "§l" + holder.getName() + " collected " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"):finalMessage) + " §r" + ((player.getTeam() != null)?((player.getTeam().equals(homeTeam)?"Kill them to return it to the base!":"Protect them at all costs.")):"")));
        }
    }

    public void crystalDead(Location location, boolean message) {
        this.state = CrystalState.DEAD;
        if (this.holder != null) {
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Captured").put("player", holder.getName())));
            if (EngineAPI.getActiveGame().getGameVariation() != null) {
                ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onCrystalDead(this);
            }
            this.holder.getStats().addProgress(AuroraMCAPI.getAchievement(62), 1, this.holder.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(62), 0), true);
            this.holder.getInventory().setContents((ItemStack[]) this.holder.getGameData().remove("crystal_inventory"));
            this.holder.getGameData().remove("crystal_possession");
            this.holder.removePotionEffect(PotionEffectType.REGENERATION);
            this.holder.setFoodLevel(25);


            if (message) {
                this.holder.getRewards().addXp("Crystal Capture", 50);
                this.holder.getStats().incrementStatistic(1, "crystalsCaptured", 1, true);

                ChatColor team = ChatColor.RED;
                if (homeTeam instanceof CQBlue) {
                    team = ChatColor.BLUE;
                }

                TextComponent title = new TextComponent(homeTeam.getName() + ((isBoss())?" Boss Crystal Captured!":" Tower Crystal Captured!"));
                title.setColor(team);
                title.setBold(true);

                TextComponent subtitle = new TextComponent(holder.getByDisguiseName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"));
                subtitle.setBold(false);
                subtitle.setColor(ChatColor.WHITE);

                String finalMessage = team + "§l" + holder.getByDisguiseName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!");
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.sendTitle(title, ((player.equals(holder) && player.isDisguised() && player.getPreferences().isHideDisguiseNameEnabled())?new TextComponent(holder.getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!")):subtitle), 20, 100, 20);
                    player.sendMessage(TextFormatter.pluginMessage("Game", ((player.equals(holder) && player.isDisguised() && player.getPreferences().isHideDisguiseNameEnabled())?team + "§l" + holder.getName() + " captured " + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal!":"Tower Crystal!"):finalMessage)));
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
        EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", homeTeam.getName() + " Teams " + ((boss)?"Boss ":"Tower " + (type)) + " Crystal Returned").put("player", holder.getName())));
        if (EngineAPI.getActiveGame().getGameVariation() != null) {
            ((CrystalQuestVariation)EngineAPI.getActiveGame().getGameVariation()).onCrystalReturned(this);
        }
        this.holder.removePotionEffect(PotionEffectType.REGENERATION);
        this.holder.setFoodLevel(25);
        if (!this.holder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(64))) {
            this.holder.getStats().achievementGained(AuroraMCAPI.getAchievement(64), 1, true);
        }
        this.holder = null;
        crystal = EngineAPI.getMapWorld().spawn(home, EnderCrystal.class);
        crystal.setCustomName(homeTeam.getName() + type);
        state = CrystalState.AT_HOME;

        ChatColor team = ChatColor.RED;
        if (homeTeam instanceof CQBlue) {
            team = ChatColor.BLUE;
        }
        TextComponent title = new TextComponent(homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal Returned!":"Tower Crystal Returned!"));
        title.setBold(true);
        title.setColor(team);

        String finalMessage = team + "§l" + homeTeam.getName() + "'s " + ((isBoss())?"Boss Crystal Returned!":"Tower Crystal Returned!");
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendTitle(title, new TextComponent(""), 20, 100, 20);
            player.sendMessage(TextFormatter.pluginMessage("Game", finalMessage));
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

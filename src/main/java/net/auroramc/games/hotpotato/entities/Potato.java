/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.entities;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameSession;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

public class Potato {

    private AuroraMCGamePlayer oldHolder;
    private AuroraMCGamePlayer holder;
    private long lastPassed;

    public Potato() {
        holder = null;
        oldHolder = null;
        lastPassed = -1;
    }

    public void newHolder(AuroraMCGamePlayer holder) {
        if (this.holder != null) {
            if (oldHolder != null) {
                if (oldHolder.equals(holder)) {
                    if (!this.holder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(167))) {
                        this.holder.getStats().achievementGained(AuroraMCAPI.getAchievement(167), 1, true);
                    }
                }
            }
            EngineAPI.getActiveGame().getGameSession().log(new GameSession.GameLogEntry(GameSession.GameEvent.GAME_EVENT, new JSONObject().put("description", "Potato Given").put("player", this.holder.getName()).put("to", holder.getName())));
            this.oldHolder = this.holder;
            lastPassed = System.currentTimeMillis();
            this.holder.getInventory().clear();
            this.holder.removePotionEffect(PotionEffectType.SPEED);
            this.holder.getGameData().remove("potato_holder");
            PlayerInventory inventory = this.holder.getInventory();
            inventory.setBoots(new ItemStack(Material.AIR));
            inventory.setLeggings(new ItemStack(Material.AIR));
            inventory.setChestplate(new ItemStack(Material.AIR));
            inventory.setHelmet(new ItemStack(Material.AIR));
            holder.getGameData().put("had_potato", true);
        }
        this.holder = holder;
        this.holder.getGameData().put("potato_holder", this);
        this.holder.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1, false, false));
        this.holder.sendMessage(TextFormatter.pluginMessage("Game", "You've been given the Hot Potato! Get rid of it before it explodes by punching a player!"));
        PlayerInventory inventory = this.holder.getInventory();
        inventory.setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short)0, false, Color.fromRGB(255, 0, 0)).getItemStack());
        inventory.setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short)0, false, Color.fromRGB(255, 0, 0)).getItemStack());
        inventory.setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short)0, false, Color.fromRGB(255, 0, 0)).getItemStack());
        inventory.setHelmet(new GUIItem(Material.TNT).getItemStack());
        Firework firework = this.holder.getLocation().getWorld().spawn(this.holder.getEyeLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 0)).trail(true).flicker(true).with(FireworkEffect.Type.BURST).build());
        firework.setFireworkMeta(meta);
        new BukkitRunnable(){
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(ServerAPI.getCore(), 2);
        ItemStack stack = new GUIItem(Material.POTATO_ITEM, "&c&lHot Potato", 1, ";&rPunch a player to get rid of the hot potato!").getItemStack();
        for (int i = 0;i < 36;i++) {
            holder.getInventory().setItem(i, stack);
        }
    }

    public void explode() {
        JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
        int x, y, z;
        x = specSpawn.getInt("x");
        y = specSpawn.getInt("y");
        z = specSpawn.getInt("z");
        float yaw = specSpawn.getFloat("yaw");
        if (holder.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT)) {
            ((DeathEffect)holder.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT)).onDeath(holder);
        }
        holder.getGameData().clear();
        holder.teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
        holder.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);

        holder.setLastHitAt(-1);
        holder.setLastHitBy(null);
        holder.getLatestHits().clear();
        holder.getInventory().clear();
        holder.getInventory().setHelmet(new ItemStack(Material.AIR));
        holder.getInventory().setChestplate(new ItemStack(Material.AIR));
        holder.getInventory().setLeggings(new ItemStack(Material.AIR));
        holder.getInventory().setBoots(new ItemStack(Material.AIR));
        holder.setFireTicks(0);

        for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
            player2.hidePlayer(holder);
            player2.sendMessage(TextFormatter.pluginMessage("Death", ((holder.isDisguised())?holder.getActiveDisguise().getName():holder.getName())));
            player2.playSound(holder.getLocation(), Sound.EXPLODE, 1, 100);
        }

        holder.setSpectator(true, true);

        TextComponent title = new TextComponent("You Died!");
        title.setColor(ChatColor.RED.asBungee());
        title.setBold(true);

        TextComponent subtitle = new TextComponent("You were holding the Hot Potato when it exploded!");
        subtitle.setColor(ChatColor.WHITE.asBungee());
        subtitle.setBold(false);

        holder.sendTitle(title, subtitle, 20, 100, 20);
        if (lastPassed == -1) {
            if (!this.holder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(171))) {
                this.holder.getStats().achievementGained(AuroraMCAPI.getAchievement(171), 1, true);
            }
        } else {
            if (System.currentTimeMillis() - lastPassed <= 3000) {
                this.oldHolder.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "closeCalls", 1, true);
                if (!this.holder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(164))) {
                    this.holder.getStats().achievementGained(AuroraMCAPI.getAchievement(164), 1, true);
                }
                if (!this.oldHolder.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(161))) {
                    this.oldHolder.getStats().achievementGained(AuroraMCAPI.getAchievement(161), 1, true);
                }
            }
        }
    }


    public AuroraMCGamePlayer getHolder() {
        return holder;
    }
}

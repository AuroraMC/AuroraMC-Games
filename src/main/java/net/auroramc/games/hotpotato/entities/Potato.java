/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.hotpotato.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

public class Potato {

    private AuroraMCGamePlayer holder;

    public Potato() {
        holder = null;
    }

    public void newHolder(AuroraMCGamePlayer holder) {
        if (this.holder != null) {
            this.holder.getPlayer().getInventory().clear();
            this.holder.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            this.holder.getGameData().remove("potato_holder");
        }
        this.holder = holder;
        this.holder.getGameData().put("potato_holder", this);
        this.holder.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 0, false, false));
        this.holder.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You've been given the Hot Potato! Get rid of it before it explodes by punching a player!"));
        Firework firework = this.holder.getPlayer().getLocation().getWorld().spawn(this.holder.getPlayer().getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 0)).trail(true).flicker(true).with(FireworkEffect.Type.BALL).build());
        firework.setFireworkMeta(meta);
        new BukkitRunnable(){
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 1);
        ItemStack stack = new GUIItem(Material.POTATO_ITEM, "&c&lHot Potato", 1, ";&rPunch a player to get rid of the hot potato!").getItem();
        for (int i = 0;i < 36;i++) {
            holder.getPlayer().getInventory().setItem(i, stack);
        }
    }

    public void explode() {
        JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
        int x, y, z;
        x = specSpawn.getInt("x");
        y = specSpawn.getInt("y");
        z = specSpawn.getInt("z");
        float yaw = specSpawn.getFloat("yaw");
        holder.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
        holder.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);

        holder.setLastHitAt(-1);
        holder.setLastHitBy(null);
        holder.getLatestHits().clear();
        holder.getPlayer().getInventory().clear();
        holder.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        holder.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
        holder.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
        holder.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
        holder.getPlayer().setFireTicks(0);

        for (Player player2 : Bukkit.getOnlinePlayers()) {
            player2.hidePlayer(holder.getPlayer());
            player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Death", ((holder.isDisguised())?holder.getActiveDisguise().getName():holder.getName())));
            player2.playSound(holder.getPlayer().getLocation(), Sound.EXPLODE, 1, 100);
        }

        holder.setSpectator(true, true);
        holder.sendTitle(AuroraMCAPI.getFormatter().convert("&c&lYou Died!"), AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight("You were holding the Hot Potato when it exploded!")), 20, 100, 20, ChatColor.RED, ChatColor.RESET, true, false);
    }

}

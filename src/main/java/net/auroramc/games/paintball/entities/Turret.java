/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.teams.PBBlue;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class Turret {

    private static final ItemStack RED_HEAD;
    private static final ItemStack BLUE_HEAD;

    static {
        init: {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/4b4b148778377b7ce25c8dfb346a5d9429dfff72f12f67d0886826a1f7266a0\"}}}".getBytes())));

            RED_HEAD = new ItemStack(Material.SKULL_ITEM, 1);
            RED_HEAD.setDurability((short)3);
            SkullMeta meta = (SkullMeta) RED_HEAD.getItemMeta();
            Field field;
            try {
                field = meta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                BLUE_HEAD = null;
                break init;
            }
            field.setAccessible(true);
            try {
                field.set(meta, profile);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            RED_HEAD.setItemMeta(meta);

            profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/e0b0b3c047ca73a3f40597efd4e87cfbe2124a3c3a67b8665f358e66eb4e91c5\"}}}".getBytes())));

            BLUE_HEAD = new ItemStack(Material.SKULL_ITEM, 1);
            BLUE_HEAD.setDurability((short)3);
            meta = (SkullMeta) BLUE_HEAD.getItemMeta();
            try {
                field = meta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                break init;
            }
            field.setAccessible(true);
            try {
                field.set(meta, profile);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            BLUE_HEAD.setItemMeta(meta);
        }
    }

    private final Location location;
    private final ArmorStand armorStand;
    private final BukkitTask task;
    private final AuroraMCPlayer owner;

    public Turret(AuroraMCPlayer owner, Location location) {
        this.location = location;
        this.armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.setHelmet(((owner.getTeam() instanceof PBBlue)?BLUE_HEAD:RED_HEAD));
        Color data = ((owner.getTeam() instanceof PBBlue)?Color.BLUE:Color.RED);
        armorStand.setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short)0, false, data).getItem());
        armorStand.setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short)0, false, data).getItem());
        armorStand.setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short)0, false, data).getItem());
        armorStand.setItemInHand(new ItemStack(((owner.getTeam() instanceof PBBlue)?Material.DIAMOND_BARDING:Material.GOLD_BARDING)));
        this.owner = owner;

        task = new BukkitRunnable(){

            int lifetime = 0;

            @Override
            public void run() {
                List<Entity> closeEntities = armorStand.getNearbyEntities(12, 12, 12);
                if (closeEntities.size() > 0) {
                    Player closest = null;
                    for (Entity entity : closeEntities) {
                        if (closest == null && entity instanceof Player) {
                            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) entity);
                            if (!player.isSpectator() && !player.getTeam().equals(owner.getTeam())) {
                                closest = (Player) entity;
                            }
                        } else if (entity instanceof Player && closest.getLocation().distanceSquared(armorStand.getLocation()) > entity.getLocation().distanceSquared(armorStand.getLocation())) {
                            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) entity);
                            if (!player.isSpectator() && !player.getTeam().equals(owner.getTeam())) {
                                closest = (Player) entity;
                            }
                        }
                    }
                    if (closest != null) {
                        Vector vector = closest.getLocation().toVector();
                        vector.subtract(armorStand.getLocation().toVector());
                        armorStand.teleport(armorStand.getLocation().setDirection(vector));
                        vector.normalize();
                        armorStand.launchProjectile(Snowball.class, vector);
                    }
                }
                lifetime++;
                if (lifetime >= 30) {
                    armorStand.remove();
                    owner.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "One of your turrets has expired!"));
                    this.cancel();
                }
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 20, 20);
    }


    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public Location getLocation() {
        return location;
    }

    public AuroraMCPlayer getOwner() {
        return owner;
    }

    public BukkitTask getTask() {
        return task;
    }
}
/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.gui.RobotMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class MiningRobot {

    private final static String[] names = {"Craig","Michael","Sean","David","Barbra","Elliot","Josh","Martha","Susan","Marcus","Alex","Oliver","Sam","Ben","Dom","Richard","Harry","Ant","Dec","Steven","Susy","Margret","Kate","Kieron","Marc","Sie","Peter","Zorp","Zeep","Blorp","WALLE","Dave","Nathan"};
    private final static ItemStack head;

    static {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/4dbc7d4e35429cbba4995e574251b50b9ed50deaaf0be96e462de886b7f6dff5\"}}}".getBytes())));

        head = new ItemStack(Material.SKULL_ITEM, 1);
        head.setDurability((short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            try {
                field.set(meta, profile);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            }
            head.setItemMeta(meta);
        } catch (NoSuchFieldException | SecurityException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

    private final Team team;
    private int level;
    private ArmorStand entity;
    private final Location location;
    private int emeralds;
    private final Map<AuroraMCPlayer, RobotInventory> inventories;
    private BukkitTask ironTask;
    private BukkitTask goldTask;
    private BukkitTask emeraldTask;

    public MiningRobot(Team team, Location location) {
        this.team = team;
        this.location = location;
        this.level = 0;
        this.inventories = new HashMap<>();
    }

    public void spawn() {
        this.level = 1;
        this.entity = location.getWorld().spawn(location, ArmorStand.class);
        this.entity.setBasePlate(false);
        this.entity.setSmall(true);
        this.entity.setArms(true);
        this.entity.setHelmet(head);
        this.entity.setItemInHand(new ItemStack(Material.DIAMOND_PICKAXE));
        this.entity.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        this.entity.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        this.entity.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        this.entity.setCustomName("§3§l" + names[new Random().nextInt(names.length)]);
        this.entity.setCustomNameVisible(true);

        for (AuroraMCPlayer player : team.getPlayers()) {
            this.inventories.put(player, new RobotInventory((AuroraMCGamePlayer) player, this));
        }

        ironTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (AuroraMCPlayer player : team.getPlayers()) {
                    inventories.get(player).addIron(5);
                }
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 200, 200);

        goldTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (AuroraMCPlayer player : team.getPlayers()) {
                    inventories.get(player).addGold(3);
                }
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 400, 400);

        emeraldTask = new BukkitRunnable() {
            @Override
            public void run() {
                emeralds++;
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 600, 600);
    }

    public void upgrade() {
        this.level++;
        goldTask.cancel();
        emeraldTask.cancel();
        if (level == 2) {
            ironTask.cancel();
            ironTask = new BukkitRunnable() {
                @Override
                public void run() {
                    for (AuroraMCPlayer player : team.getPlayers()) {
                        inventories.get(player).addIron(7);
                    }
                }
            }.runTaskTimer(EngineAPI.getGameEngine(), 140, 140);

            goldTask = new BukkitRunnable() {
                @Override
                public void run() {
                    for (AuroraMCPlayer player : team.getPlayers()) {
                        inventories.get(player).addGold(4);
                    }
                }
            }.runTaskTimer(EngineAPI.getGameEngine(), 280, 280);

            emeraldTask = new BukkitRunnable() {
                @Override
                public void run() {
                    emeralds++;
                }
            }.runTaskTimer(EngineAPI.getGameEngine(), 400, 400);
        } else {
            goldTask = new BukkitRunnable() {
                @Override
                public void run() {
                    for (AuroraMCPlayer player : team.getPlayers()) {
                        inventories.get(player).addGold(6);
                    }
                }
            }.runTaskTimer(EngineAPI.getGameEngine(), 140, 140);

            emeraldTask = new BukkitRunnable() {
                @Override
                public void run() {
                    emeralds++;
                }
            }.runTaskTimer(EngineAPI.getGameEngine(), 280, 280);
        }
    }

    public int getLevel() {
        return level;
    }

    public ArmorStand getEntity() {
        return entity;
    }

    public Team getTeam() {
        return team;
    }

    public Location getLocation() {
        return location;
    }

    public int getEmeralds() {
        return emeralds;
    }

    public void addEmeralds(int emeralds) {
        this.emeralds += emeralds;
    }

    public int withdrawEmeralds() {
        int oldEmeralds = emeralds;
        this.emeralds = 0;
        return oldEmeralds;
    }

    public Map<AuroraMCPlayer, RobotInventory> getInventories() {
        return inventories;
    }

    public void openGUI(AuroraMCPlayer player) {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            if (ServerAPI.getGUI(player1) != null && ServerAPI.getGUI(player1) instanceof RobotMenu) {
                RobotMenu menu = (RobotMenu) ServerAPI.getGUI(player1);
                if (menu.getRobot().equals(this)) {
                    player.sendMessage(TextFormatter.pluginMessage("Game", "Someone is already in the Robot's menu!"));
                    return;
                }
            }
        }
        RobotMenu menu = new RobotMenu((AuroraMCServerPlayer)player, this);
        menu.open((AuroraMCServerPlayer) player);
    }
}

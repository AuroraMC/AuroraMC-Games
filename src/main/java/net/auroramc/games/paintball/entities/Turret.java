/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.entities;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class Turret {

    private final Location location;
    private final Snowman snowman;
    private BukkitTask task;
    private AuroraMCPlayer owner;

    public Turret(AuroraMCPlayer owner, Location location) {
        this.location = location;
        this.snowman = location.getWorld().spawn(location, Snowman.class);
        this.owner = owner;
        CraftEntity craftEntity = ((CraftEntity)snowman);
        NBTTagCompound tag = craftEntity.getHandle().getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        craftEntity.getHandle().c(tag);
        tag.setInt("NoAI", 1);
        tag.setInt("Invulnerable", 1);
        craftEntity.getHandle().f(tag);

        task = new BukkitRunnable(){

            int lifetime = 0;

            @Override
            public void run() {
                List<Entity> closeEntities = snowman.getNearbyEntities(12, 12, 12);
                if (closeEntities.size() > 0) {
                    Player closest = null;
                    for (Entity entity : closeEntities) {
                        if (closest == null && entity instanceof Player) {
                            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) entity);
                            if (!player.isSpectator() && !player.getTeam().equals(owner.getTeam())) {
                                closest = (Player) entity;
                            }
                        } else if (entity instanceof Player && closest.getLocation().distanceSquared(snowman.getLocation()) > entity.getLocation().distanceSquared(snowman.getLocation())) {
                            AuroraMCGamePlayer player = (AuroraMCGamePlayer) AuroraMCAPI.getPlayer((Player) entity);
                            if (!player.isSpectator() && !player.getTeam().equals(owner.getTeam())) {
                                closest = (Player) entity;
                            }
                        }
                    }
                    if (closest != null) {
                        Vector vector = closest.getLocation().toVector();
                        vector.subtract(snowman.getLocation().toVector());
                        vector.normalize();
                        snowman.teleport(snowman.getLocation().setDirection(vector));
                        snowman.launchProjectile(Snowball.class, vector);
                    }
                }
                lifetime++;
                if (lifetime >= 30) {
                    snowman.remove();
                    owner.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "One of your turrets has expired!"));
                    this.cancel();
                }
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 20, 20);
    }


    public Snowman getSnowman() {
        return snowman;
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

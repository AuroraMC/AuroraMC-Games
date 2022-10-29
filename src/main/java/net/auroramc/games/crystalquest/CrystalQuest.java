/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.entities.CrystalQuestScoreboardRunnable;
import net.auroramc.games.crystalquest.kits.Defender;
import net.auroramc.games.crystalquest.kits.Fighter;
import net.auroramc.games.crystalquest.kits.Miner;
import net.auroramc.games.crystalquest.listeners.*;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import net.auroramc.games.util.listeners.death.DeathRespawnListener;
import net.auroramc.games.util.listeners.settings.PregameMoveListener;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import netscape.javascript.JSObject;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CrystalQuest extends Game {

    public static final ItemStack compass;

    private final ShowListener showListener;
    private final ShopListener shopListener;
    private final InventoryListener inventoryListener;
    private final MiningListener miningListener;
    private final CrystalListener crystalListener;
    private final ChestListener chestListener;
    private final KitListener kitListener;
    private BukkitTask mineTask;

    private BukkitTask destroyTask;
    private BukkitTask endTask;
    private BukkitTask compassTask;
    private BukkitTask protectionTask;

    private List<BukkitTask> tasks;

    private BukkitTask scoreboardTask;

    private boolean redUnlucky;
    private boolean blueUnlucky;

    static {
        compass = new GUIItem(Material.COMPASS, "&3Crystal Compass", 1, ";&rThe compass will display the distance;&rfrom the closest crystal currently captured.").getItem();
    }


    public CrystalQuest(GameVariation gameVariation) {
        super(gameVariation);
        showListener = new ShowListener();
        shopListener = new ShopListener();
        miningListener = new MiningListener();
        inventoryListener = new InventoryListener();
        crystalListener = new CrystalListener();
        chestListener = new ChestListener();
        kitListener = new KitListener();
        this.teams.put("Blue", new CQBlue());
        this.teams.put("Red", new CQRed());
        this.kits.add(new Miner());
        this.kits.add(new Defender());
        this.kits.add(new Fighter());
        //this.kits.add(new Healer());
        //this.kits.add(new Ecologist());
        this.tasks = new ArrayList<>();
        blueUnlucky = true;
        redUnlucky = true;
    }

    @Override
    public void preLoad() {
    }

    @Override
    public void load(GameMap gameMap) {
        this.map = gameMap;
        ((CQBlue)this.teams.get("Blue")).loadRobots(map);
        ((CQRed)this.teams.get("Red")).loadRobots(map);

        //Generate the mine.
        generateMine(0.145f, 0.1f, 0.005f);

        //Now spawn shops.

        //Disable tile drops for explosions.
        EngineAPI.getMapWorld().setGameRuleValue("doTileDrops", "false");
    }

    @Override
    public void start() {
        super.start();
        DeathRespawnListener.register(100, false);
        PregameMoveListener.register();
        Bukkit.getPluginManager().registerEvents(showListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(shopListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(inventoryListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(miningListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(crystalListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(chestListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(kitListener, EngineAPI.getGameEngine());

        int redSpawnIndex = 0;
        int blueSpawnIndex = 0;
        JSONArray redSpawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("RED");
        JSONArray blueSpawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("BLUE");
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            AuroraMCGamePlayer gp = (AuroraMCGamePlayer) player;
            if (gp.isSpectator()) {
                JSONObject specSpawn = this.map.getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
                int x, y, z;
                x = specSpawn.getInt("x");
                y = specSpawn.getInt("y");
                z = specSpawn.getInt("z");
                float yaw = specSpawn.getFloat("yaw");
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
            } else {
                if (gp.getTeam() instanceof CQRed) {
                    JSONObject spawn = redSpawns.getJSONObject(redSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                    redSpawnIndex++;
                    if (redSpawnIndex >= redSpawns.length()) {
                        redSpawnIndex = 0;
                    }
                } else {
                    JSONObject spawn = blueSpawns.getJSONObject(blueSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x + 0.5, y, z + 0.5, yaw, 0));
                    blueSpawnIndex++;
                    if (blueSpawnIndex >= blueSpawns.length()) {
                        blueSpawnIndex = 0;
                    }
                }
                gp.getKit().onGameStart(player);
            }
        }

        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            PlayerScoreboard scoreboard = player.getScoreboard();
            scoreboard.setTitle("&3&l-= &b&lCRYSTAL QUEST &3&l=-");
            scoreboard.setLine(14, "&9&l«BLUE CRYSTALS»");
            scoreboard.setLine(13, "&a✰ &a✰ &a✰");
            scoreboard.setLine(12, " ");
            scoreboard.setLine(11, "&c&l«RED CRYSTALS»");
            scoreboard.setLine(10, "&a✰ &a✰ &a✰");
            scoreboard.setLine(9, "    ");
            scoreboard.setLine(8, "&b&l«TEAM LIVES»");
            scoreboard.setLine(7, "0");
            scoreboard.setLine(6, "  ");
            scoreboard.setLine(5, "&b&l«MINE RESET»");
            scoreboard.setLine(4, "3.0 minutes ");
            scoreboard.setLine(3, "   ");
            scoreboard.setLine(2, "&b&l«GAME TIME»");
            scoreboard.setLine(1, "0.0 minutes");

        }

        //Spawn shops
        JSONObject shops = map.getMapData().getJSONObject("game").getJSONObject("SHOP");
        for (Object obj : shops.getJSONArray("PLAYER")) {
            JSONObject playerShop = (JSONObject) obj;
            Location location = new Location(EngineAPI.getMapWorld(), playerShop.getInt("x") + 0.5, playerShop.getInt("y"), playerShop.getInt("z") + 0.5, playerShop.getFloat("yaw"), 0);
            Villager villager = EngineAPI.getMapWorld().spawn(location, Villager.class);
            CraftEntity craftEntity = ((CraftEntity)villager);
            NBTTagCompound tag = craftEntity.getHandle().getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            craftEntity.getHandle().c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Invulnerable", 1);
            craftEntity.getHandle().f(tag);
            ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
            stand.setVisible(false);
            stand.setCustomName(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight("&3&lPlayer Shop")));
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setMarker(true);
            Rabbit rabbit = location.getWorld().spawn(location, Rabbit.class);
            rabbit.setPassenger(stand);
            rabbit.setBaby();
            craftEntity = ((CraftEntity)rabbit);
            tag = craftEntity.getHandle().getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            craftEntity.getHandle().c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Invulnerable", 1);
            craftEntity.getHandle().f(tag);
            rabbit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
            villager.setPassenger(rabbit);
        }

        for (Object obj : shops.getJSONArray("TEAM")) {
            JSONObject playerShop = (JSONObject) obj;
            Location location = new Location(EngineAPI.getMapWorld(), playerShop.getInt("x") + 0.5, playerShop.getInt("y"), playerShop.getInt("z") + 0.5, playerShop.getFloat("yaw"), 0);
            Villager villager = EngineAPI.getMapWorld().spawn(location, Villager.class);
            CraftEntity craftEntity = ((CraftEntity)villager);
            NBTTagCompound tag = craftEntity.getHandle().getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            craftEntity.getHandle().c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Invulnerable", 1);
            craftEntity.getHandle().f(tag);
            ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
            stand.setVisible(false);
            stand.setCustomName(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight("&3&lTeam Shop")));
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setMarker(true);
            Rabbit rabbit = location.getWorld().spawn(location, Rabbit.class);
            rabbit.setPassenger(stand);
            rabbit.setBaby();
            craftEntity = ((CraftEntity)rabbit);
            tag = craftEntity.getHandle().getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            craftEntity.getHandle().c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Invulnerable", 1);
            craftEntity.getHandle().f(tag);
            rabbit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
            villager.setPassenger(rabbit);
        }

    }

    private void generateMine(float iron, float gold, float emerald) {
        JSONArray dataLocations = map.getMapData().getJSONArray("data_locations");
        int emeralds = Math.round(emerald * dataLocations.length());
        int ironOres = Math.round(iron * dataLocations.length());
        int goldOres = Math.round(gold * dataLocations.length());

        List<Location> locations = new ArrayList<>();
        for (int i = 0;i < dataLocations.length();i++) {
            JSONObject object = dataLocations.getJSONObject(i);
            Location location = new Location(EngineAPI.getMapWorld(), object.getInt("x") + 0.5, object.getInt("y"), object.getInt("z") + 0.5);
            locations.add(location);
        }
        Collections.shuffle(locations);

        for (Location location : locations) {
            if (emeralds > 0) {
                location.getBlock().setType(Material.EMERALD_ORE);
                emeralds--;
            } else if (ironOres > 0) {
                location.getBlock().setType(Material.IRON_ORE);
                ironOres--;
            } else if (goldOres > 0) {
                location.getBlock().setType(Material.GOLD_ORE);
                goldOres--;
            } else {
                location.getBlock().setType(Material.STONE);
            }
        }
    }

    @Override
    public void end(AuroraMCPlayer winner) {
        onEnd();
        super.end(winner);
    }

    @Override
    public void end(Team winner, String winnerName) {
        onEnd();
        for (AuroraMCPlayer player : winner.getPlayers()) {
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(61))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(61), 1, true);
            }
        }
        if (winner instanceof CQBlue) {
            CQRed red = (CQRed) teams.get("Red");
            CQBlue blue = (CQBlue) winner;
            if (redUnlucky) {
                checkForUnlucky(blue.getBossCrystal(), blue.getTowerBCrystal(), blue.getTowerACrystal(), red.getPlayers());
            }
        } else {
            CQBlue blue = (CQBlue) teams.get("Blue");
            CQRed red = (CQRed) winner;
            if (blueUnlucky) {
                checkForUnlucky(red.getBossCrystal(), red.getTowerBCrystal(), red.getTowerACrystal(), blue.getPlayers());
            }
        }
        super.end(winner, winnerName);
    }

    private void checkForUnlucky(Crystal bossCrystal, Crystal towerBCrystal, Crystal towerACrystal, List<AuroraMCPlayer> players) {
        if (bossCrystal.getState() == Crystal.CrystalState.DEAD && towerBCrystal.getState() == Crystal.CrystalState.DEAD && towerACrystal.getState() == Crystal.CrystalState.DEAD) {
            for (AuroraMCPlayer player : players) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(63))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(63), 1, true);
                }
            }
        }
    }

    private void onEnd() {
        if (mineTask != null) {
            this.mineTask.cancel();
            this.mineTask = null;
        }
        if (scoreboardTask != null) {
            scoreboardTask.cancel();
        }
        if (compassTask != null) {
            compassTask.cancel();
        }
        if (endTask != null) {
            endTask.cancel();
        }
        if (destroyTask != null) {
            destroyTask.cancel();
        }
        if (protectionTask != null) {
            protectionTask.cancel();
        }
        PlayerShowEvent.getHandlerList().unregister(showListener);
        PlayerInteractAtEntityEvent.getHandlerList().unregister(shopListener);
        InventoryOpenEvent.getHandlerList().unregister(shopListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(shopListener);
        InventoryClickEvent.getHandlerList().unregister(inventoryListener);
        PlayerDropItemEvent.getHandlerList().unregister(inventoryListener);
        PlayerItemConsumeEvent.getHandlerList().unregister(inventoryListener);
        PlayerInteractEvent.getHandlerList().unregister(inventoryListener);
        BlockBreakEvent.getHandlerList().unregister(miningListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(crystalListener);
        PlayerInteractAtEntityEvent.getHandlerList().unregister(crystalListener);
        FoodLevelChangeEvent.getHandlerList().unregister(miningListener);
        BlockPlaceEvent.getHandlerList().unregister(miningListener);
        PlayerPickupItemEvent.getHandlerList().unregister(miningListener);
        BlockFromToEvent.getHandlerList().unregister(miningListener);
        PlayerInteractEvent.getHandlerList().unregister(chestListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(kitListener);
        DeathRespawnListener.unregister();
        PregameMoveListener.unregister();

        if (!starting) {
            CQRed red = (CQRed) getTeams().get("Red");
            red.getTowerBCrystal().unregisterListener();
            red.getTowerACrystal().unregisterListener();
            red.getBossCrystal().unregisterListener();

            CQBlue blue = (CQBlue) getTeams().get("Blue");
            blue.getTowerBCrystal().unregisterListener();
            blue.getTowerACrystal().unregisterListener();
            blue.getBossCrystal().unregisterListener();
        }

        for (BukkitTask task : tasks) {
            task.cancel();
        }
    }

    @Override
    public void inProgress() {
        super.inProgress();
        PregameMoveListener.unregister();
        new BukkitRunnable(){
            @Override
            public void run() {
                ((CQRed)teams.get("Red")).getRobotSlotA().spawn();
                ((CQBlue)teams.get("Blue")).getRobotSlotA().spawn();
                //Now spawn the crystals.
                CQRed red = (CQRed) teams.get("Red");

                JSONObject object = map.getMapData().getJSONObject("game").getJSONObject("CRYSTAL");
                JSONArray tower = object.getJSONArray("TOWER RED");
                JSONObject boss = object.getJSONArray("BOSS RED").getJSONObject(0);

                Crystal bossCrystal = new Crystal(new Location(EngineAPI.getMapWorld(), boss.getInt("x") + 0.5, boss.getInt("y"), boss.getInt("z") + 0.5), red, true, "Boss");
                red.setBossCrystal(bossCrystal);
                Crystal towerA = new Crystal(new Location(EngineAPI.getMapWorld(), tower.getJSONObject(0).getInt("x") + 0.5, tower.getJSONObject(0).getInt("y"), tower.getJSONObject(0).getInt("z") + 0.5), red, false, "A");
                red.setTowerACrystal(towerA);
                Crystal towerB = new Crystal(new Location(EngineAPI.getMapWorld(), tower.getJSONObject(1).getInt("x") + 0.5, tower.getJSONObject(1).getInt("y"), tower.getJSONObject(1).getInt("z") + 0.5), red, false, "B");
                red.setTowerBCrystal(towerB);


                CQBlue blue = (CQBlue) teams.get("Blue");
                tower = object.getJSONArray("TOWER BLUE");
                boss = object.getJSONArray("BOSS BLUE").getJSONObject(0);

                bossCrystal = new Crystal(new Location(EngineAPI.getMapWorld(), boss.getInt("x") + 0.5, boss.getInt("y"), boss.getInt("z") + 0.5), blue, true, "Boss");
                blue.setBossCrystal(bossCrystal);
                towerA = new Crystal(new Location(EngineAPI.getMapWorld(), tower.getJSONObject(0).getInt("x") + 0.5, tower.getJSONObject(0).getInt("y"), tower.getJSONObject(0).getInt("z") + 0.5), blue, false, "A");
                blue.setTowerACrystal(towerA);
                towerB = new Crystal(new Location(EngineAPI.getMapWorld(), tower.getJSONObject(1).getInt("x") + 0.5, tower.getJSONObject(1).getInt("y"), tower.getJSONObject(1).getInt("z") + 0.5), blue, false, "B");
                blue.setTowerBCrystal(towerB);

                object = map.getMapData().getJSONObject("game").getJSONObject("CHEST");

                JSONObject blueLoc = object.getJSONArray("BLUE").getJSONObject(0);
                JSONObject redLoc = object.getJSONArray("RED").getJSONObject(0);
                blue.setChest(new Location(EngineAPI.getMapWorld(), blueLoc.getInt("x"), blueLoc.getInt("y"), blueLoc.getInt("z"), blueLoc.getFloat("yaw"), 0));
                red.setChest(new Location(EngineAPI.getMapWorld(), redLoc.getInt("x"), redLoc.getInt("y"), redLoc.getInt("z"),redLoc.getFloat("yaw"), 0));

                scoreboardTask = new CrystalQuestScoreboardRunnable((CQBlue) teams.get("Blue"), (CQRed) teams.get("Red")).runTaskTimer(EngineAPI.getGameEngine(), 0, 20);

            }
        }.runTask(AuroraMCAPI.getCore());
        mineTask = new BukkitRunnable() {
            @Override
            public void run() {
                generateMine(0.14f, 0.12f, 0.01f);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The mine has been reset! The next reset is in **5** minutes."));
                }
                mineTask = new BukkitRunnable(){
                    @Override
                    public void run() {
                        generateMine(0.14f, 0.14f, 0.02f);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The mine has been reset! The next reset is in **5** minutes."));
                        }
                    }
                }.runTaskTimer(AuroraMCAPI.getCore(), 6000, 6000);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 3600);
        destroyTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Crystals will be destroyed in **5 minutes**!"));
                }
                destroyTask = new BukkitRunnable(){
                    @Override
                    public void run() {
                        CQBlue blue = (CQBlue) teams.get("Blue");
                        CQRed red = (CQRed) teams.get("Red");

                        JSONArray returnPoints = EngineAPI.getActiveMap().getMapData().getJSONObject("game").getJSONObject("CRYSTAL").getJSONArray("RETURN RED");
                        JSONObject pointA = returnPoints.getJSONObject(0);
                        JSONObject pointB = returnPoints.getJSONObject(1);
                        JSONObject pointC = returnPoints.getJSONObject(2);
                        Location a = new Location(EngineAPI.getMapWorld(), pointA.getInt("x") + 0.5, pointA.getInt("y") - 0.5, pointA.getInt("z") + 0.5);
                        Location b = new Location(EngineAPI.getMapWorld(), pointB.getInt("x") + 0.5, pointB.getInt("y") - 0.5, pointB.getInt("z") + 0.5);
                        Location c = new Location(EngineAPI.getMapWorld(), pointC.getInt("x") + 0.5, pointC.getInt("y") - 0.5, pointC.getInt("z") + 0.5);

                        if (red.getTowerACrystal().getState() != Crystal.CrystalState.DEAD) {
                            blueUnlucky = false;
                            red.getTowerACrystal().crystalDead(a, false);
                        }
                        if (red.getTowerBCrystal().getState() != Crystal.CrystalState.DEAD) {
                            blueUnlucky = false;
                            red.getTowerBCrystal().crystalDead(b, false);
                        }
                        if (red.getBossCrystal().getState() != Crystal.CrystalState.DEAD) {
                            blueUnlucky = false;
                            red.getBossCrystal().crystalDead(c, false);
                        }

                        returnPoints = EngineAPI.getActiveMap().getMapData().getJSONObject("game").getJSONObject("CRYSTAL").getJSONArray("RETURN BLUE");
                        pointA = returnPoints.getJSONObject(0);
                        pointB = returnPoints.getJSONObject(1);
                        pointC = returnPoints.getJSONObject(2);
                        a = new Location(EngineAPI.getMapWorld(), pointA.getInt("x") + 0.5, pointA.getInt("y") - 0.5, pointA.getInt("z") + 0.5);
                        b = new Location(EngineAPI.getMapWorld(), pointB.getInt("x") + 0.5, pointB.getInt("y") - 0.5, pointB.getInt("z") + 0.5);
                        c = new Location(EngineAPI.getMapWorld(), pointC.getInt("x") + 0.5, pointC.getInt("y") - 0.5, pointC.getInt("z") + 0.5);

                        if (blue.getTowerACrystal().getState() != Crystal.CrystalState.DEAD) {
                            redUnlucky = false;
                            blue.getTowerACrystal().crystalDead(a, false);
                        }
                        if (blue.getTowerBCrystal().getState() != Crystal.CrystalState.DEAD) {
                            redUnlucky = false;
                            blue.getTowerBCrystal().crystalDead(b, false);
                        }
                        if (blue.getBossCrystal().getState() != Crystal.CrystalState.DEAD) {
                            redUnlucky = false;
                            blue.getBossCrystal().crystalDead(c, false);
                        }

                        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "&c&lAll Crystals have been destroyed! Last team alive wins!"));
                        }

                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 5999);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 17999);
        endTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The game will be end in **5 minutes**!"));
                }
                endTask = new BukkitRunnable(){
                    @Override
                    public void run() {
                        end(null);
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 5999);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 47999);
        compassTask = new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                    AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
                    if (!player.isSpectator()) {
                        CQBlue blue = (CQBlue) teams.get("Blue");
                        CQRed red = (CQRed) teams.get("Red");
                        if (player.getTeam().equals(blue)) {
                            if (blue.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getBossCrystal().getHolder().getPlayer().getLocation());
                            } else if (blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getTowerACrystal().getHolder().getPlayer().getLocation());
                            } else if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getTowerBCrystal().getHolder().getPlayer().getLocation());
                            } else if (red.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getBossCrystal().getHolder().getPlayer().getLocation());
                            } else if (red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getTowerACrystal().getHolder().getPlayer().getLocation());
                            } else if (red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getTowerBCrystal().getHolder().getPlayer().getLocation());
                            } else {
                                Location closest = null;
                                if (red.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    closest = red.getBossCrystal().getHome();
                                }
                                if (red.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > red.getTowerACrystal().getHome().distanceSquared(player.getPlayer().getLocation())) {
                                        closest = red.getTowerACrystal().getHome();
                                    }
                                }
                                if (red.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > red.getTowerBCrystal().getHome().distanceSquared(player.getPlayer().getLocation())) {
                                        closest = red.getTowerBCrystal().getHome();
                                    }
                                }
                                if (closest == null) {
                                    for (AuroraMCPlayer pl1 : AuroraMCAPI.getPlayers()) {
                                        AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) pl1;
                                        if (!player1.isSpectator() && !player1.getTeam().equals(player.getTeam())) {
                                            if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > player1.getPlayer().getLocation().distanceSquared(player.getPlayer().getLocation())) {
                                                closest = player1.getPlayer().getLocation();
                                            }
                                        }
                                    }
                                    player.getPlayer().setCompassTarget(closest);
                                } else {
                                    player.getPlayer().setCompassTarget(closest);
                                }
                            }
                        } else {
                            if (red.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getBossCrystal().getHolder().getPlayer().getLocation());
                            } else if (red.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getTowerACrystal().getHolder().getPlayer().getLocation());
                            } else if (red.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(red.getTowerBCrystal().getHolder().getPlayer().getLocation());
                            } else if (blue.getBossCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getBossCrystal().getHolder().getPlayer().getLocation());
                            } else if (blue.getTowerACrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getTowerACrystal().getHolder().getPlayer().getLocation());
                            } else if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.CAPTURED) {
                                player.getPlayer().setCompassTarget(blue.getTowerBCrystal().getHolder().getPlayer().getLocation());
                            } else {
                                Location closest = null;
                                if (blue.getBossCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    closest = blue.getBossCrystal().getHome();
                                }
                                if (blue.getTowerACrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > blue.getTowerACrystal().getHome().distanceSquared(player.getPlayer().getLocation())) {
                                        closest = blue.getTowerACrystal().getHome();
                                    }
                                }
                                if (blue.getTowerBCrystal().getState() == Crystal.CrystalState.AT_HOME) {
                                    if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > blue.getTowerBCrystal().getHome().distanceSquared(player.getPlayer().getLocation())) {
                                        closest = blue.getTowerBCrystal().getHome();
                                    }
                                }
                                if (closest == null) {
                                    for (AuroraMCPlayer pl1 : AuroraMCAPI.getPlayers()) {
                                        AuroraMCGamePlayer player1 = (AuroraMCGamePlayer) pl1;
                                        if (!player1.isSpectator() && !player1.getTeam().equals(player.getTeam())) {
                                            if (closest == null || closest.distanceSquared(player.getPlayer().getLocation()) > player1.getPlayer().getLocation().distanceSquared(player.getPlayer().getLocation())) {
                                                closest = player1.getPlayer().getLocation();
                                            }
                                        }
                                    }
                                    player.getPlayer().setCompassTarget(closest);
                                } else {
                                    player.getPlayer().setCompassTarget(closest);
                                }
                            }
                        }

                        if (player.getPlayer().getInventory().contains(Material.EMERALD, 64)) {
                            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(68))) {
                                player.getStats().achievementGained(AuroraMCAPI.getAchievement(68), 1, true);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(EngineAPI.getGameEngine(), 0, 20);
        protectionTask = new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                    pl.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You can now capture crystals!"));
                }
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 2399);
        for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
            pl.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Crystals are protected for the first 2 minutes of the game!"));
            AuroraMCGamePlayer player = (AuroraMCGamePlayer) pl;
            if (!player.isSpectator() && player.getKit() instanceof Defender) {
                switch (player.getKitLevel().getLatestUpgrade()) {
                    case 0: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 12) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem());
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 240, 240));
                        break;
                    }
                    case 1: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 12) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(stack);
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 220, 220));
                        break;
                    }
                    case 2: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 12) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(stack);
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 200, 200));
                        break;
                    }
                    case 3: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 14) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(stack);
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 180, 180));
                        break;
                    }
                    case 4: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 16) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(stack);
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 160, 160));
                        break;
                    }
                    case 5: {
                        tasks.add(new BukkitRunnable() {

                            private final ItemStack stack = new GUIItem(Material.STAINED_GLASS, null, 1, null, (short)((player.getTeam() instanceof CQBlue)?11:14)).getItem();

                            @Override
                            public void run() {
                                if (player.getPlayer().isOnline()) {
                                    if (!player.getPlayer().getInventory().contains(Material.STAINED_GLASS, 16) && !player.isSpectator()) {
                                        player.getPlayer().getInventory().addItem(stack);
                                    }
                                } else {
                                    this.cancel();
                                }

                            }
                        }.runTaskTimer(EngineAPI.getGameEngine(), 120, 120));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void generateTeam(AuroraMCPlayer auroraMCPlayer) {

    }

    @Override
    public void onPlayerJoin(Player player) {
        JSONObject specSpawn = EngineAPI.getActiveMap().getMapData().getJSONObject("spawn").getJSONArray("SPECTATOR").getJSONObject(0);
        int x, y, z;
        x = specSpawn.getInt("x");
        y = specSpawn.getInt("y");
        z = specSpawn.getInt("z");
        float yaw = specSpawn.getFloat("yaw");
        player.teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(player);
                }
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 2);
    }

    @Override
    public void onPlayerJoin(AuroraMCGamePlayer auroraMCGamePlayer) {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(auroraMCGamePlayer.getPlayer());
                }
                auroraMCGamePlayer.setSpectator(true, true);
            }
        }.runTask(AuroraMCAPI.getCore());
    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer player) {
        if (player.getTeam() != null) {
            if (player.getTeam() instanceof CQRed) {
                CQRed red = (CQRed) player.getTeam();
                red.getPlayers().remove(player);
                if (red.getPlayers().size() == 0) {
                    this.end(teams.get("Blue"), null);
                    return;
                }
            } else if (player.getTeam() instanceof CQBlue) {
                CQBlue blue = (CQBlue) player.getTeam();
                blue.getPlayers().remove(player);
                if (blue.getPlayers().size() == 0) {
                    this.end(teams.get("Red"), null);
                    return;
                }
            }

            if (player.getTeam() instanceof CQBlue) {
                CQBlue blue = (CQBlue) player.getTeam();
                if (player.getGameData().containsKey("crystal_possession")) {
                    CQRed red = (CQRed) this.getTeams().get("Red");
                    player.getPlayer().getInventory().setContents((ItemStack[]) player.getGameData().remove("crystal_inventory"));
                    String crystal = (String) player.getGameData().remove("crystal_possession");
                    if (crystal.equals("BOSS")) {
                        red.getBossCrystal().crystalReturned();
                    } else if (crystal.equals("A")) {
                        red.getTowerACrystal().crystalReturned();
                    } else {
                        red.getTowerBCrystal().crystalReturned();
                    }
                }
            } else {
                CQRed red = (CQRed) player.getTeam();
                if (player.getGameData().containsKey("crystal_possession")) {
                    CQBlue blue = (CQBlue) this.getTeams().get("Blue");
                    player.getPlayer().getInventory().setContents((ItemStack[]) player.getGameData().remove("crystal_inventory"));
                    String crystal = (String) player.getGameData().remove("crystal_possession");
                    if (crystal.equals("BOSS")) {
                        blue.getBossCrystal().crystalReturned();
                    } else if (crystal.equals("A")) {
                        blue.getTowerACrystal().crystalReturned();
                    } else {
                        blue.getTowerBCrystal().crystalReturned();
                    }
                }
            }

            AuroraMCGamePlayer killer = null;
            KillMessage killMessage;
            KillMessage.KillReason killReason = KillMessage.KillReason.MELEE;
            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                killMessage = (KillMessage) player.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
            } else {
                killMessage = (KillMessage) AuroraMCAPI.getCosmetics().get(500);
            }

            if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                killer = player.getLastHitBy();
            }

            if (killer != null) {
                if (killer.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                    killMessage = (KillMessage) killer.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
                }
                killer.getRewards().addXp("Kills", 25);
                killer.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "kills", 1, true);

                //If there is a killer, give out assists.
                for (Map.Entry<AuroraMCGamePlayer, Long> entry : player.getLatestHits().entrySet()) {
                    if (System.currentTimeMillis() - entry.getValue() < 60000 && entry.getKey().getId() != killer.getId()) {
                        entry.getKey().getRewards().addXp("Assists", 10);
                        entry.getKey().getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", "You got an assist on player **" + player.getPlayer().getName() + "**!"));
                        entry.getKey().getPlayer().playSound(entry.getKey().getPlayer().getLocation(), Sound.ARROW_HIT, 100, 1);
                    }
                }
            }

            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths", 1, true);
            player.getStats().incrementStatistic(EngineAPI.getActiveGameInfo().getId(), "deaths." + killReason.name(), 1, true);
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Kill", killMessage.onKill(AuroraMCAPI.getPlayer(player2), killer, player, null, killReason, EngineAPI.getActiveGameInfo().getId())));
            }
        }
    }

    @Override
    public void onRespawn(AuroraMCGamePlayer player) {
        Location location;
        if (player.getTeam() instanceof CQRed) {
            JSONArray spawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("RED");
            JSONObject spawn = spawns.getJSONObject(new Random().nextInt(spawns.length()));
            int x, y, z;
            x = spawn.getInt("x");
            y = spawn.getInt("y");
            z = spawn.getInt("z");
            float yaw = spawn.getFloat("yaw");
            location = new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0);

        } else {
            JSONArray spawns = this.map.getMapData().getJSONObject("spawn").getJSONArray("BLUE");
            JSONObject spawn = spawns.getJSONObject(new Random().nextInt(spawns.length()));
            int x, y, z;
            x = spawn.getInt("x");
            y = spawn.getInt("y");
            z = spawn.getInt("z");
            float yaw = spawn.getFloat("yaw");
            location = new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0);
        }
        player.getPlayer().teleport(location);
        if (player.getGameData().containsKey("death_inventory")) {
            player.getPlayer().getInventory().setContents((ItemStack[]) player.getGameData().get("death_inventory"));
            player.getPlayer().getInventory().setHelmet((ItemStack) player.getGameData().get("death_helmet"));
            player.getPlayer().getInventory().setChestplate((ItemStack) player.getGameData().get("death_chestplate"));
            player.getPlayer().getInventory().setLeggings((ItemStack) player.getGameData().get("death_leggings"));
            player.getPlayer().getInventory().setBoots((ItemStack) player.getGameData().get("death_boots"));
        } else {
            player.getPlayer().getInventory().setHelmet((ItemStack) player.getGameData().get("death_helmet"));
            player.getPlayer().getInventory().setChestplate((ItemStack) player.getGameData().get("death_chestplate"));
            player.getPlayer().getInventory().setLeggings((ItemStack) player.getGameData().get("death_leggings"));
            player.getPlayer().getInventory().setBoots((ItemStack) player.getGameData().get("death_boots"));

            player.getPlayer().getInventory().setItem(0, (ItemStack) player.getGameData().get("death_sword"));
            player.getPlayer().getInventory().setItem(1, (ItemStack) player.getGameData().get("death_pickaxe"));
            player.getPlayer().getInventory().setItem(2, (ItemStack) player.getGameData().get("death_axe"));
            player.getPlayer().getInventory().setItem(8, compass);
        }

        player.getGameData().clear();

        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        player.getPlayer().setHealth(20.0D);
        player.getPlayer().setFoodLevel(30);
        player.getPlayer().setExp(0.0F);
        player.getPlayer().setLevel(0);
        player.getPlayer().setFlying(false);
        player.getPlayer().setAllowFlight(false);
    }

    @Override
    public boolean onDeath(AuroraMCGamePlayer player, AuroraMCGamePlayer killer) {
        boolean life;
        boolean finalKill;
        if (player.getTeam() instanceof CQBlue) {
            CQBlue blue = (CQBlue) player.getTeam();
            if (player.getGameData().containsKey("crystal_possession")) {
                    CQRed red = (CQRed) this.getTeams().get("Red");
                    player.getPlayer().getInventory().setContents((ItemStack[]) player.getGameData().remove("crystal_inventory"));
                    String crystal = (String) player.getGameData().remove("crystal_possession");
                    if (crystal.equals("BOSS")) {
                        red.getBossCrystal().crystalReturned();
                    } else if (crystal.equals("A")) {
                        red.getTowerACrystal().crystalReturned();
                    } else {
                        red.getTowerBCrystal().crystalReturned();
                    }
                    if (killer != null) {
                        if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(66))) {
                            killer.getStats().achievementGained(AuroraMCAPI.getAchievement(66), 1, true);
                        }
                    }
            }
            if (blue.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                finalKill = true;
                life = false;
            } else {
                life = ((CQBlue) player.getTeam()).getLives() > 0;
                finalKill = false;
                if (life) {
                    ((CQBlue) player.getTeam()).lostLife();
                }
            }
        } else {
            CQRed red = (CQRed) player.getTeam();
            if (player.getGameData().containsKey("crystal_possession")) {
                CQBlue blue = (CQBlue) this.getTeams().get("Blue");
                player.getPlayer().getInventory().setContents((ItemStack[]) player.getGameData().remove("crystal_inventory"));
                String crystal = (String) player.getGameData().remove("crystal_possession");
                if (crystal.equals("BOSS")) {
                    blue.getBossCrystal().crystalReturned();
                } else if (crystal.equals("A")) {
                    blue.getTowerACrystal().crystalReturned();
                } else {
                    blue.getTowerBCrystal().crystalReturned();
                }
            }
            if (red.getBossCrystal().getState() == Crystal.CrystalState.DEAD) {
                finalKill = true;
                life = false;
            } else {
                life = ((CQRed) player.getTeam()).getLives() > 0;
                finalKill = false;
                if (life) {
                    ((CQRed) player.getTeam()).lostLife();
                }
            }
        }
        if (killer != null) {
            killer.getStats().addProgress(AuroraMCAPI.getAchievement(65), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(65), 0), true);
            if (killer.getPlayer().getHealth() >= 19) {
                if (!killer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(72))) {
                    killer.getStats().achievementGained(AuroraMCAPI.getAchievement(72), 1, true);
                }
            }
        }
        if (!finalKill) {
            if (life) {
                player.getGameData().put("death_helmet", player.getPlayer().getInventory().getHelmet());
                player.getGameData().put("death_chestplate", player.getPlayer().getInventory().getChestplate());
                player.getGameData().put("death_leggings", player.getPlayer().getInventory().getLeggings());
                player.getGameData().put("death_boots", player.getPlayer().getInventory().getBoots());
                player.getGameData().put("death_inventory", player.getPlayer().getInventory().getContents());
            } else {
                Map<Integer, ? extends ItemStack> gold = player.getPlayer().getInventory().all(Material.GOLD_INGOT);
                Map<Integer, ? extends ItemStack> iron = player.getPlayer().getInventory().all(Material.IRON_INGOT);
                Map<Integer, ? extends ItemStack> emeralds = player.getPlayer().getInventory().all(Material.EMERALD);

                int amountOfGold = 0, amountOfIron = 0, amountOfEmeralds = 0;

                for (Map.Entry<Integer, ? extends ItemStack> entry : gold.entrySet()) {
                    amountOfGold += entry.getValue().getAmount();
                }
                for (Map.Entry<Integer, ? extends ItemStack> entry : iron.entrySet()) {
                    amountOfIron += entry.getValue().getAmount();
                }
                for (Map.Entry<Integer, ? extends ItemStack> entry : emeralds.entrySet()) {
                    amountOfEmeralds += entry.getValue().getAmount();
                }

                amountOfGold = amountOfGold / 2;
                amountOfIron = amountOfIron / 2;
                amountOfEmeralds = amountOfEmeralds / 2;

                if (killer != null) {
                    List<String> builder = new ArrayList<>();
                    if (amountOfIron > 0) {
                        builder.add("&7+" + amountOfIron + " Iron");
                    }
                    if (amountOfGold > 0) {
                        builder.add("&6+" + amountOfGold + " Gold");
                    }
                    if (amountOfEmeralds > 0) {
                        builder.add("&a+" + amountOfEmeralds + " Emeralds");
                    }

                    if (builder.size() > 0) {
                        killer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().convert(String.join("\n", builder)));
                    }
                }

                while (amountOfGold > 0) {
                    if (amountOfGold > 64) {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, 64));
                        }
                        amountOfGold -= 64;
                    } else {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amountOfGold));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, amountOfGold));
                        }
                        amountOfGold = 0;
                    }
                }

                while (amountOfIron > 0) {
                    if (amountOfIron > 64) {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.IRON_INGOT, 64));
                        }
                        amountOfIron -= 64;
                    } else {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, amountOfIron));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.IRON_INGOT, amountOfIron));
                        }
                        amountOfIron = 0;
                    }
                }

                while (amountOfEmeralds > 0) {
                    if (amountOfEmeralds > 64) {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.EMERALD, 64));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.EMERALD, 64));
                        }
                        amountOfEmeralds -= 64;
                    } else {
                        if (killer != null) {
                            Map<Integer, ItemStack> items = killer.getPlayer().getInventory().addItem(new ItemStack(Material.EMERALD, amountOfEmeralds));
                            if (items.size() > 0) {
                                for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
                                    player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), item.getValue());
                                }
                            }
                        } else {
                            player.getPlayer().getLocation().getWorld().dropItemNaturally(player.getPlayer().getLocation(), new ItemStack(Material.EMERALD, amountOfEmeralds));
                        }
                        amountOfEmeralds = 0;
                    }
                }
                switch (player.getPlayer().getInventory().getHelmet().getType()) {
                    case LEATHER_HELMET: {
                        player.getGameData().put("death_helmet", player.getPlayer().getInventory().getHelmet());
                        break;
                    }
                    case CHAINMAIL_HELMET: {
                        ItemStack stack = player.getPlayer().getInventory().getHelmet();
                        stack.setType(Material.LEATHER_HELMET);
                        player.getGameData().put("death_helmet", stack);
                        break;
                    }
                    case IRON_HELMET: {
                        ItemStack stack = player.getPlayer().getInventory().getHelmet();
                        stack.setType(Material.CHAINMAIL_HELMET);
                        player.getGameData().put("death_helmet", stack);
                        break;
                    }
                    case DIAMOND_HELMET: {
                        ItemStack stack = player.getPlayer().getInventory().getHelmet();
                        stack.setType(Material.IRON_HELMET);
                        player.getGameData().put("death_helmet", stack);
                        break;
                    }
                }
                switch (player.getPlayer().getInventory().getChestplate().getType()) {
                    case LEATHER_CHESTPLATE: {
                        player.getGameData().put("death_chestplate", player.getPlayer().getInventory().getChestplate());
                        break;
                    }
                    case CHAINMAIL_CHESTPLATE: {
                        ItemStack stack = player.getPlayer().getInventory().getChestplate();
                        if (!(player.getKit() instanceof Fighter)) {
                            stack.setType(Material.LEATHER_CHESTPLATE);
                        }
                        player.getGameData().put("death_chestplate", stack);
                        break;
                    }
                    case IRON_CHESTPLATE: {
                        ItemStack stack = player.getPlayer().getInventory().getChestplate();
                        stack.setType(Material.CHAINMAIL_CHESTPLATE);
                        player.getGameData().put("death_chestplate", stack);
                        break;
                    }
                    case DIAMOND_CHESTPLATE: {
                        ItemStack stack = player.getPlayer().getInventory().getChestplate();
                        stack.setType(Material.IRON_CHESTPLATE);
                        player.getGameData().put("death_chestplate", stack);
                        break;
                    }
                }
                switch (player.getPlayer().getInventory().getLeggings().getType()) {
                    case LEATHER_LEGGINGS: {
                        player.getGameData().put("death_leggings", player.getPlayer().getInventory().getLeggings());
                        break;
                    }
                    case CHAINMAIL_LEGGINGS: {
                        ItemStack stack = player.getPlayer().getInventory().getLeggings();
                        stack.setType(Material.LEATHER_LEGGINGS);
                        player.getGameData().put("death_leggings", stack);
                        break;
                    }
                    case IRON_LEGGINGS: {
                        ItemStack stack = player.getPlayer().getInventory().getLeggings();
                        stack.setType(Material.CHAINMAIL_LEGGINGS);
                        player.getGameData().put("death_leggings", stack);
                        break;
                    }
                    case DIAMOND_LEGGINGS: {
                        ItemStack stack = player.getPlayer().getInventory().getLeggings();
                        stack.setType(Material.IRON_LEGGINGS);
                        player.getGameData().put("death_leggings", stack);
                        break;
                    }
                }
                switch (player.getPlayer().getInventory().getBoots().getType()) {
                    case LEATHER_BOOTS: {
                        player.getGameData().put("death_boots", player.getPlayer().getInventory().getBoots());
                        break;
                    }
                    case CHAINMAIL_BOOTS: {
                        ItemStack stack = player.getPlayer().getInventory().getBoots();
                        stack.setType(Material.LEATHER_BOOTS);
                        player.getGameData().put("death_boots", stack);
                        break;
                    }
                    case IRON_BOOTS: {
                        ItemStack stack = player.getPlayer().getInventory().getBoots();
                        stack.setType(Material.CHAINMAIL_BOOTS);
                        player.getGameData().put("death_boots", stack);
                        break;
                    }
                    case DIAMOND_BOOTS: {
                        ItemStack stack = player.getPlayer().getInventory().getBoots();
                        stack.setType(Material.IRON_BOOTS);
                        player.getGameData().put("death_boots", stack);
                        break;
                    }
                }

                int swordSlot = 0, pickSlot = 1, axeSlot = 2;

                for (int i = 0; i < 36; i++) {
                    if (player.getPlayer().getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                        swordSlot = i;
                    } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_AXE")) {
                        axeSlot = i;
                    } else if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_PICKAXE")) {
                        pickSlot = i;
                    }
                }
                switch (player.getPlayer().getInventory().getItem(swordSlot).getType()) {
                    case STONE_SWORD: {
                        player.getGameData().put("death_sword", player.getPlayer().getInventory().getItem(swordSlot));
                        break;
                    }
                    case IRON_SWORD: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(swordSlot);
                        stack.setType(Material.STONE_SWORD);
                        player.getGameData().put("death_sword", stack);
                        break;
                    }
                    case DIAMOND_SWORD: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(swordSlot);
                        stack.setType(Material.IRON_SWORD);
                        player.getGameData().put("death_sword", stack);
                        break;
                    }
                }

                switch (player.getPlayer().getInventory().getItem(pickSlot).getType()) {
                    case STONE_PICKAXE: {
                        player.getGameData().put("death_pickaxe", player.getPlayer().getInventory().getItem(pickSlot));
                        break;
                    }
                    case IRON_PICKAXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(pickSlot);
                        stack.setType(Material.STONE_PICKAXE);
                        player.getGameData().put("death_pickaxe", stack);
                        break;
                    }
                    case DIAMOND_PICKAXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(pickSlot);
                            if (stack.getEnchantmentLevel(Enchantment.DIG_SPEED) < 2) {
                                stack.setType(Material.IRON_PICKAXE);
                            } else {
                                if (!(player.getKit() instanceof Miner)) {
                                    stack.removeEnchantment(Enchantment.DIG_SPEED);
                                }
                            }
                        player.getGameData().put("death_pickaxe", stack);
                        break;
                    }
                }

                switch (player.getPlayer().getInventory().getItem(axeSlot).getType()) {
                    case WOOD_AXE:
                    case STONE_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(axeSlot);
                        stack.setType(Material.WOOD_AXE);
                        player.getGameData().put("death_axe", stack);
                        break;
                    }
                    case IRON_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(axeSlot);
                        stack.setType(Material.STONE_AXE);
                        player.getGameData().put("death_axe", stack);
                        break;
                    }
                    case DIAMOND_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(axeSlot);
                        stack.setType(Material.IRON_AXE);
                        stack.removeEnchantment(Enchantment.DIG_SPEED);
                        player.getGameData().put("death_axe", stack);
                        break;
                    }
                }
            }
        }
        return finalKill;
    }

    @Override
    public void onFinalKill(AuroraMCGamePlayer player) {
        if (player.getTeam().getPlayers().stream().noneMatch(auroraMCPlayer -> (!auroraMCPlayer.isDead()))) {
            if (player.getTeam() instanceof CQBlue) {
                //Red won.
                this.end(teams.get("Red"), null);
            } else {
                //Blue won
                this.end(teams.get("Blue"), null);
            }
        }
    }
}



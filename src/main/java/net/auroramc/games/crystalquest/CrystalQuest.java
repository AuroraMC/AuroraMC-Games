/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Game;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.entities.ScoreboardRunnable;
import net.auroramc.games.crystalquest.kits.Defender;
import net.auroramc.games.crystalquest.kits.Fighter;
import net.auroramc.games.crystalquest.kits.Miner;
import net.auroramc.games.crystalquest.listeners.*;
import net.auroramc.games.crystalquest.teams.CQBlue;
import net.auroramc.games.crystalquest.teams.CQRed;
import net.auroramc.games.util.listeners.DeathRespawnListener;
import net.auroramc.games.util.listeners.PregameMoveListener;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CrystalQuest extends Game {

    public static final ItemStack compass;

    private final ShowListener showListener;
    private final ShopListener shopListener;
    private final InventoryListener inventoryListener;
    private final MiningListener miningListener;
    private final CrystalListener crystalListener;
    private BukkitTask mineTask;

    private BukkitTask scoreboardTask;

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
        this.teams.put("Blue", new CQBlue());
        this.teams.put("Red", new CQRed());
        this.kits.add(new Miner());
        this.kits.add(new Defender());
        this.kits.add(new Fighter());
    }

    @Override
    public void preLoad() {
    }

    @Override
    public void load(GameMap gameMap) {
        this.map = gameMap;

        //Generate the mine.
        generateMine(0.145f, 0.1f, 0.005f);

        //Now spawn shops.
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
            craftEntity.getHandle().f(tag);
            rabbit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
            villager.setPassenger(rabbit);
        }

        //Disable tile drops for explosions.
        EngineAPI.getMapWorld().setGameRuleValue("doTileDrops", "false");
    }

    @Override
    public void start() {
        super.start();
        DeathRespawnListener.register(200, false);
        PregameMoveListener.register();
        Bukkit.getPluginManager().registerEvents(showListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(shopListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(inventoryListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(miningListener, EngineAPI.getGameEngine());
        Bukkit.getPluginManager().registerEvents(crystalListener, EngineAPI.getGameEngine());

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
                gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
            } else {
                if (gp.getTeam() instanceof CQRed) {
                    JSONObject spawn = redSpawns.getJSONObject(redSpawnIndex);
                    int x, y, z;
                    x = spawn.getInt("x");
                    y = spawn.getInt("y");
                    z = spawn.getInt("z");
                    float yaw = spawn.getFloat("yaw");
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
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
                    gp.getPlayer().teleport(new Location(EngineAPI.getMapWorld(), x, y, z, yaw, 0));
                    blueSpawnIndex++;
                    if (blueSpawnIndex >= blueSpawns.length()) {
                        blueSpawnIndex = 0;
                    }
                }
                gp.getKit().onGameStart(player);
            }
        }

        new BukkitRunnable(){
            @Override
            public void run() {
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

                scoreboardTask = new ScoreboardRunnable((CQBlue) teams.get("Blue"), (CQRed) teams.get("Red")).runTaskTimer(EngineAPI.getGameEngine(), 0, 20);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 20);
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
        super.end(winner, winnerName);
    }

    private void onEnd() {
        if (mineTask != null) {
            this.mineTask.cancel();
            this.mineTask = null;
        }
        scoreboardTask.cancel();
        PlayerShowEvent.getHandlerList().unregister(showListener);
        PlayerInteractAtEntityEvent.getHandlerList().unregister(shopListener);
        InventoryOpenEvent.getHandlerList().unregister(shopListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(shopListener);
        InventoryClickEvent.getHandlerList().unregister(inventoryListener);
        PlayerDropItemEvent.getHandlerList().unregister(inventoryListener);
        BlockBreakEvent.getHandlerList().unregister(miningListener);
        EntityDamageByEntityEvent.getHandlerList().unregister(crystalListener);
        PlayerInteractAtEntityEvent.getHandlerList().unregister(crystalListener);
        FoodLevelChangeEvent.getHandlerList().unregister(miningListener);
        DeathRespawnListener.unregister();

        CQRed red = (CQRed) getTeams().get("Red");
        red.getTowerBCrystal().unregisterListener();
        red.getTowerACrystal().unregisterListener();
        red.getBossCrystal().unregisterListener();

        CQBlue blue = (CQBlue) getTeams().get("Blue");
        blue.getTowerBCrystal().unregisterListener();
        blue.getTowerACrystal().unregisterListener();
        blue.getBossCrystal().unregisterListener();
    }

    @Override
    public void inProgress() {
        super.inProgress();
        PregameMoveListener.unregister();
        ((CQRed)this.teams.get("Red")).getRobotSlotA().spawn();
        ((CQBlue)this.teams.get("Blue")).getRobotSlotA().spawn();
        mineTask = new BukkitRunnable() {
            @Override
            public void run() {
                generateMine(0.1415f, 0.1010f, 0.0075f);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The mine has been reset! The next reset is in **5** minutes."));
                }
                mineTask = new BukkitRunnable(){
                    @Override
                    public void run() {
                        generateMine(0.12f, 0.12f, 0.01f);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "The mine has been reset! The next reset is in **5** minutes."));
                        }
                    }
                }.runTaskTimer(AuroraMCAPI.getCore(), 6000, 6000);
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 3600);
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
                auroraMCGamePlayer.setSpectator(true, true);
            }
        }.runTask(AuroraMCAPI.getCore());
    }

    @Override
    public void onPlayerLeave(AuroraMCGamePlayer auroraMCGamePlayer) {

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
    public boolean onDeath(AuroraMCGamePlayer player) {
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

        if (!finalKill) {
            if (life) {
                player.getGameData().put("death_helmet", player.getPlayer().getInventory().getHelmet());
                player.getGameData().put("death_chestplate", player.getPlayer().getInventory().getChestplate());
                player.getGameData().put("death_leggings", player.getPlayer().getInventory().getLeggings());
                player.getGameData().put("death_boots", player.getPlayer().getInventory().getBoots());
                player.getGameData().put("death_inventory", player.getPlayer().getInventory().getContents());
            } else {
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

                switch (player.getPlayer().getInventory().getItem(0).getType()) {
                    case STONE_SWORD: {
                        player.getGameData().put("death_sword", player.getPlayer().getInventory().getItem(0));
                        break;
                    }
                    case IRON_SWORD: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(0);
                        stack.setType(Material.STONE_SWORD);
                        player.getGameData().put("death_sword", stack);
                        break;
                    }
                    case DIAMOND_SWORD: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(0);
                        stack.setType(Material.IRON_SWORD);
                        player.getGameData().put("death_sword", stack);
                        break;
                    }
                }

                switch (player.getPlayer().getInventory().getItem(1).getType()) {
                    case STONE_PICKAXE: {
                        player.getGameData().put("death_pickaxe", player.getPlayer().getInventory().getItem(1));
                        break;
                    }
                    case IRON_PICKAXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(1);
                        stack.setType(Material.STONE_PICKAXE);
                        player.getGameData().put("death_pickaxe", stack);
                        break;
                    }
                    case DIAMOND_PICKAXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(1);
                        if (!((player.getKit() instanceof Miner))) {
                            stack.setType(Material.IRON_PICKAXE);
                        }
                        player.getGameData().put("death_pickaxe", stack);
                        break;
                    }
                }

                switch (player.getPlayer().getInventory().getItem(2).getType()) {
                    case WOOD_AXE:
                    case STONE_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(2);
                        stack.setType(Material.WOOD_AXE);
                        player.getGameData().put("death_axe", stack);
                        break;
                    }
                    case IRON_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(2);
                        stack.setType(Material.STONE_AXE);
                        player.getGameData().put("death_axe", stack);
                        break;
                    }
                    case DIAMOND_AXE: {
                        ItemStack stack = player.getPlayer().getInventory().getItem(2);
                        stack.setType(Material.IRON_AXE);
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


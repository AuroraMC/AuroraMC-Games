/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.teams;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Team;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.entities.MiningRobot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CQRed implements Team {

    private final List<AuroraMCPlayer> players;
    private int crystalsCaptured;
    private Crystal bossCrystal;
    private Crystal towerACrystal;
    private Crystal towerBCrystal;
    private int lives;

    private int protUpgrade;
    private int powerUpgrade;
    private int sharpUpgrade;

    private MiningRobot robotSlotA;
    private MiningRobot robotSlotB;
    private MiningRobot robotSlotC;

    private Block chest;

    public CQRed() {
        players = new ArrayList<>();
        crystalsCaptured = 0;
    }

    public void loadRobots(GameMap map) {
        JSONArray locations = map.getMapData().getJSONObject("game").getJSONObject("MININGROBOT").getJSONArray("RED");
        JSONObject jsonA = locations.getJSONObject(0);
        JSONObject jsonB = locations.getJSONObject(1);
        JSONObject jsonC = locations.getJSONObject(2);
        Location a = new Location(EngineAPI.getMapWorld(), jsonA.getInt("x") + 0.5, jsonA.getInt("y"), jsonA.getInt("z") + 0.5, jsonA.getFloat("yaw"), 0);
        Location b = new Location(EngineAPI.getMapWorld(), jsonB.getInt("x") + 0.5, jsonB.getInt("y"), jsonB.getInt("z") + 0.5, jsonB.getFloat("yaw"), 0);
        Location c = new Location(EngineAPI.getMapWorld(), jsonC.getInt("x") + 0.5, jsonC.getInt("y"), jsonC.getInt("z") + 0.5, jsonC.getFloat("yaw"), 0);

        protUpgrade = powerUpgrade = sharpUpgrade = 0;
        robotSlotA = new MiningRobot(this, a);
        robotSlotB = new MiningRobot(this, b);
        robotSlotC = new MiningRobot(this, c);
    }

    public void setChest(Location location) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        BlockState chest = block.getState();
        BlockFace face;
        float yaw = location.getYaw();
        if (yaw <= -135 || yaw >= 135) {
            face = BlockFace.NORTH;
        } else if (yaw > -135 && yaw < -45) {
            face = BlockFace.EAST;
        } else if (yaw >= -45 && yaw <= 45) {
            face = BlockFace.SOUTH;
        } else {
            face = BlockFace.WEST;
        }
        chest.setData(new Chest(face));
        chest.update();
        this.chest = block;
    }


    @Override
    public int getId() {
        return 0;
    }

    public int getCrystalsCaptured() {
        return crystalsCaptured;
    }

    public void setCrystalsCaptured(int crystalsCaptured) {
        this.crystalsCaptured = crystalsCaptured;
    }

    @Override
    public ChatColor getTeamColor() {
        return ChatColor.RED;
    }

    @Override
    public String getName() {
        return "Red";
    }

    @Override
    public List<AuroraMCPlayer> getPlayers() {
        return players;
    }

    public Crystal getBossCrystal() {
        return bossCrystal;
    }

    public Crystal getTowerACrystal() {
        return towerACrystal;
    }

    public Crystal getTowerBCrystal() {
        return towerBCrystal;
    }

    public void setBossCrystal(Crystal bossCrystal) {
        this.bossCrystal = bossCrystal;
    }

    public void setTowerACrystal(Crystal towerACrystal) {
        this.towerACrystal = towerACrystal;
    }

    public void setTowerBCrystal(Crystal towerBCrystal) {
        this.towerBCrystal = towerBCrystal;
    }

    public int getLives() {
        return lives;
    }

    public void lostLife() {
        lives--;
        for (AuroraMCPlayer player : players) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "One of your players died, so you lost a life! You now have **" + lives + "** lives!"));
        }
    }

    public boolean lifeBrought() {
        if (lives >= 5) {
            return false;
        }
        lives++;
        for (AuroraMCPlayer player : players) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "You now have an additional life! You now have **" + lives + "** lives!"));
        }
        return true;
    }

    public int getPowerUpgrade() {
        return powerUpgrade;
    }

    public void upgradePower() {
        powerUpgrade++;
        if (powerUpgrade == 2 && protUpgrade == 2 && sharpUpgrade == 2 && robotSlotA.getEntity() != null && robotSlotB.getEntity() != null && robotSlotC.getEntity() != null) {
            for (AuroraMCPlayer player : players) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(70))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(70), 1, true);
                }
            }
        }
        for (AuroraMCPlayer player : players) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "Your **Power Upgrade** was upgraded to **Level " + powerUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                int i = ((AuroraMCServerPlayer)player).getInventory().first(Material.BOW);
                if (i > -1) {
                    ItemStack stack = ((AuroraMCServerPlayer)player).getInventory().getItem(i);
                    stack.addEnchantment(Enchantment.ARROW_DAMAGE, powerUpgrade);
                }
            } else if (((AuroraMCGamePlayer) player).getGameData().containsKey("death_inventory")) {
                ItemStack[] stack = (ItemStack[]) ((AuroraMCGamePlayer) player).getGameData().get("death_inventory");
                for (ItemStack item : stack) {
                    if (item.getType() == Material.BOW) {
                        item.addEnchantment(Enchantment.ARROW_DAMAGE, powerUpgrade);
                    }
                }
            }
        }
    }

    public int getProtUpgrade() {
        return protUpgrade;
    }

    public void upgradeProt() {
        protUpgrade++;
        if (powerUpgrade == 2 && protUpgrade == 2 && sharpUpgrade == 2 && robotSlotA.getEntity() != null && robotSlotB.getEntity() != null && robotSlotC.getEntity() != null) {
            for (AuroraMCPlayer player : players) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(70))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(70), 1, true);
                }
            }
        }
        for (AuroraMCPlayer player : players) {
            ((AuroraMCServerPlayer)player).sendMessage(TextFormatter.pluginMessage("Game", "Your **Protection Upgrade** was upgraded to **Level " + protUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                ((AuroraMCServerPlayer)player).getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                ((AuroraMCServerPlayer)player).getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                ((AuroraMCServerPlayer)player).getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                ((AuroraMCServerPlayer)player).getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
            } else {
                if (!((AuroraMCServerPlayer)player).isDead()) {
                    ((ItemStack)((AuroraMCGamePlayer) player).getGameData().get("death_helmet")).addEnchantment(Enchantment.DAMAGE_ALL, protUpgrade);
                    ((ItemStack)((AuroraMCGamePlayer) player).getGameData().get("death_chestplate")).addEnchantment(Enchantment.DAMAGE_ALL, protUpgrade);
                    ((ItemStack)((AuroraMCGamePlayer) player).getGameData().get("death_leggings")).addEnchantment(Enchantment.DAMAGE_ALL, protUpgrade);
                    ((ItemStack)((AuroraMCGamePlayer) player).getGameData().get("death_boots")).addEnchantment(Enchantment.DAMAGE_ALL, protUpgrade);
                }
            }
        }
    }

    public int getSharpUpgrade() {
        return sharpUpgrade;
    }

    public void upgradeSharp() {
        sharpUpgrade++;
        if (powerUpgrade == 2 && protUpgrade == 2 && sharpUpgrade == 2 && robotSlotA.getEntity() != null && robotSlotB.getEntity() != null && robotSlotC.getEntity() != null) {
            for (AuroraMCPlayer player : players) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(70))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(70), 1, true);
                }
            }
        }
        for (AuroraMCPlayer player : players) {
            player.sendMessage(TextFormatter.pluginMessage("Game", "Your **Sharpness Upgrade** was upgraded to **Level " + sharpUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                int slot = -1;
                for (int i = 0; i < 36; i++) {
                    if (((AuroraMCServerPlayer)player).getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (((AuroraMCServerPlayer)player).getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                        slot = i;
                        break;
                    }
                }
                if (slot > -1) {
                    ItemStack stack = ((AuroraMCServerPlayer)player).getInventory().getItem(slot);
                    stack.addEnchantment(Enchantment.DAMAGE_ALL, sharpUpgrade);
                }
            } else {
                if (((AuroraMCGamePlayer) player).getGameData().containsKey("death_inventory")) {
                    ItemStack[] stack = (ItemStack[]) ((AuroraMCGamePlayer) player).getGameData().get("death_inventory");
                    for (ItemStack item : stack) {
                        if (item.getType().name().endsWith("_SWORD")) {
                            item.addEnchantment(Enchantment.DAMAGE_ALL, sharpUpgrade);
                        }
                    }
                } else if (((AuroraMCGamePlayer) player).getGameData().containsKey("death_sword")) {
                    ((ItemStack)((AuroraMCGamePlayer) player).getGameData().get("death_sword")).addEnchantment(Enchantment.DAMAGE_ALL, sharpUpgrade);
                }
            }
        }
    }

    public MiningRobot getRobotSlotA() {
        return robotSlotA;
    }

    public MiningRobot getRobotSlotB() {
        return robotSlotB;
    }

    public MiningRobot getRobotSlotC() {
        return robotSlotC;
    }

    public MiningRobot newRobot() {
        if (robotSlotB.getLevel() == 0) {
            return robotSlotB;
        } else if (robotSlotC.getLevel() == 0) {
            return robotSlotC;
        } else {
            return null;
        }
    }

    public Block getChest() {
        return chest;
    }
}

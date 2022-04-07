/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.teams;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Team;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.GameMap;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.crystalquest.entities.Crystal;
import net.auroramc.games.crystalquest.entities.MiningRobot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CQBlue implements Team {

    private final List<AuroraMCPlayer> players;
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

    public CQBlue() {
        players = new ArrayList<>();
        lives = 0;
    }

    public void loadRobots(GameMap map) {
        JSONArray locations = map.getMapData().getJSONObject("game").getJSONObject("MININGROBOT").getJSONArray("BLUE");
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
        DirectionalContainer chest = (DirectionalContainer) block.getState().getData();
        BlockFace face;
        switch (Math.round(location.getYaw() * 10)) {
            case -1800:
                face = BlockFace.NORTH;
                break;
            case -1575:
                face = BlockFace.NORTH_NORTH_EAST;
                break;
            case -1350:
                face = BlockFace.NORTH_EAST;
                break;
            case -1125:
                face = BlockFace.EAST_NORTH_EAST;
                break;
            case -900:
                face = BlockFace.EAST;
                break;
            case -675:
                face = BlockFace.EAST_SOUTH_EAST;
                break;
            case -450:
                face = BlockFace.SOUTH_EAST;
                break;
            case -225:
                face = BlockFace.SOUTH_SOUTH_EAST;
                break;
            case 0:
                face = BlockFace.SOUTH;
                break;
            case 225:
                face = BlockFace.SOUTH_SOUTH_WEST;
                break;
            case 450:
                face = BlockFace.SOUTH_WEST;
                break;
            case 675:
                face = BlockFace.WEST_SOUTH_WEST;
                break;
            case 900:
                face = BlockFace.WEST;
                break;
            case 1125:
                face = BlockFace.WEST_NORTH_WEST;
                break;
            case 1350:
                face = BlockFace.NORTH_WEST;
                break;
            case 1575:
                face = BlockFace.NORTH_NORTH_WEST;
                break;
            default:
                face = BlockFace.NORTH;
        }
        chest.setFacingDirection(face);

        this.chest = block;
    }


    @Override
    public int getId() {
        return 1;
    }

    @Override
    public char getTeamColor() {
        return '9';
    }

    @Override
    public String getName() {
        return "Blue";
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
    }

    public boolean lifeBrought() {
        if (lives >= 5) {
            return false;
        }
        lives++;
        for (AuroraMCPlayer player : players) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "You now have an additional life! You now have **" + lives + "** lives!"));
        }
        return true;
    }

    public int getPowerUpgrade() {
        return powerUpgrade;
    }

    public void upgradePower() {
        powerUpgrade++;
        for (AuroraMCPlayer player : players) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Your **Power Upgrade** was upgraded to **Level " + powerUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                int i = player.getPlayer().getInventory().first(Material.BOW);
                if (i > -1) {
                    ItemStack stack = player.getPlayer().getInventory().getItem(i);
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
        for (AuroraMCPlayer player : players) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Your **Protection Upgrade** was upgraded to **Level " + protUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                player.getPlayer().getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                player.getPlayer().getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                player.getPlayer().getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
                player.getPlayer().getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protUpgrade);
            } else {
                if (!player.isDead()) {
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
        for (AuroraMCPlayer player : players) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game", "Your **Sharpness Upgrade** was upgraded to **Level " + sharpUpgrade + "**!"));
            if (!((AuroraMCGamePlayer) player).isSpectator()) {
                int slot = -1;
                for (int i = 0; i < 36; i++) {
                    if (player.getPlayer().getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getPlayer().getInventory().getItem(i).getType().name().endsWith("_SWORD")) {
                        slot = i;
                        break;
                    }
                }
                if (slot > -1) {
                    ItemStack stack = player.getPlayer().getInventory().getItem(slot);
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

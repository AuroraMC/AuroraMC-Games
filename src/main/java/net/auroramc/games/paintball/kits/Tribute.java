/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.paintball.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.teams.PBBlue;
import org.bukkit.Color;
import org.bukkit.Material;

public class Tribute extends Kit {


    public Tribute() {
        super(0, 3, "&cTribute", "A brave tribute, one who volunteers to fight for his team… Find a way to kill the enemy team and remove all their lives before they can do the same to you…", Material.SNOW_BALL, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.SNOW_BALL, null, 64).getItem());
        int amount = (int) ((AuroraMCGamePlayer) player).getGameData().get("gold");
        if (amount == 0) {
            amount = 1;
        }
        player.getPlayer().getInventory().setItem(8, new GUIItem(Material.GOLD_NUGGET, "&c&lShop &7- &6&l" + ((AuroraMCGamePlayer) player).getGameData().get("gold") + " Gold", amount).getItem());
        Color data = ((player.getTeam() instanceof PBBlue) ? Color.BLUE : Color.RED);
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET, null, 1, null, (short) 0, false, data).getItem());
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE, null, 1, null, (short) 0, false, data).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS, null, 1, null, (short) 0, false, data).getItem());
        player.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS, null, 1, null, (short) 0, false, data).getItem());
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1: {
                return "Reduce ‘Receive Snowball’ cooldown by 1.0 Seconds (becomes 11.0 Seconds)";
            }
            case 2: {
                return "Reduce ‘Receive Snowball’ cooldown by 1.0 Seconds (becomes 10.0 Seconds);" +
                        "&r - &bReceive +1 Snowball for Each Kill (Becomes 2 per kill)";
            }
            case 3: {
                return "Reduce ‘Receive Snowball’ cooldown by 1.0 Seconds (becomes 9.0 Seconds)";
            }
            case 4: {
                return "Reduce ‘Receive Snowball’ cooldown by 1.0 Seconds (becomes 8.0 Seconds);" +
                        "&r - &bReceive +1 Snowball for Each Kill (Becomes 3 per kill)";
            }
            case 5: {
                return "Reduce ‘Receive Snowball’ cooldown by 2.0 Seconds (becomes 6.0 Seconds);" +
                        "&r - &bReceive +2 Snowball for Each Kill (Becomes 5 per kill)";
            }
        }
        return "None";
    }
}

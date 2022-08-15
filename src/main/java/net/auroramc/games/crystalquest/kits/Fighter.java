/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.games.Kit;
import net.auroramc.games.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Fighter extends Kit {

    public Fighter() {
        super(2, 1, "&cFighter", "These players have mastered the art of aggression, they are capable of winning fights that most would deem unwinnable.", Material.DIAMOND_SWORD, 4000);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        ItemStack stack = new GUIItem(Material.LEATHER_BOOTS).getItem();
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getPlayer().getInventory().setBoots(stack);
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItem());
        player.getPlayer().getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItem());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItem();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getPlayer().getInventory().setItem(2, axe);

        player.getPlayer().getInventory().setItem(8, CrystalQuest.compass);
    }

    @Override
    public String getUpgradeReward(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4: {
                return "Receive +1% Damage Reduction.";
            }
            case 3: {
                return "Receive +1% Damage Reduction.;" +
                        "&r - &bReceive +1 Damage towards passive ability.";
            }
            case 5: {
                return "Receive +1% Damage Reduction.;" +
                        "&r - &bReceive +1 Damage towards passive ability.";
            }
        }
        return "None";
    }
}

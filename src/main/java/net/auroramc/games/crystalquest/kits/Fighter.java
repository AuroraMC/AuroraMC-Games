/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.crystalquest.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItemStack());
        ItemStack stack = new GUIItem(Material.LEATHER_BOOTS).getItemStack();
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        player.getInventory().setBoots(stack);
        player.getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).getItemStack());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItemStack());

        player.getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItemStack());
        player.getInventory().setItem(1, new GUIItem(Material.STONE_PICKAXE).getItemStack());
        ItemStack axe = new GUIItem(Material.WOOD_AXE).getItemStack();
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(2, axe);

        player.getInventory().setItem(8, CrystalQuest.compass);
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

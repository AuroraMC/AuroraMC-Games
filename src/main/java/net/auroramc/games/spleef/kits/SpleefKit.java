/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.games.spleef.kits;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpleefKit extends Kit {


    public SpleefKit() {
        super(0, 100, "&cSpleef Kit", "A kit for Spleef. Simple.", Material.SNOW_BALL, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().clear();
        switch (EngineAPI.getActiveMap().getMapData().getString("tool")) {
            case "spade": {
                ItemStack stack = new GUIItem(Material.DIAMOND_SPADE).getItem();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getPlayer().getInventory().setItem(0, stack);
                break;
            }
            case "axe": {
                ItemStack stack = new GUIItem(Material.DIAMOND_AXE).getItem();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getPlayer().getInventory().setItem(0, stack);
                break;
            }
            case "pickaxe": {
                ItemStack stack = new GUIItem(Material.DIAMOND_PICKAXE).getItem();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getPlayer().getInventory().setItem(0, stack);
                break;
            }
        }

    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

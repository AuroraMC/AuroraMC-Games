/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.games.spleef.kits;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.engine.api.EngineAPI;
import net.auroramc.engine.api.games.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Snowman extends Kit {


    public Snowman() {
        super(0, 100, "&cSnowman", "Run around and avoid getting spleefed into the lava! Receive a snowball for each block broken, and throw them to break more blocks or knock players off!", Material.SNOW_BALL, -1);
    }

    @Override
    public void onGameStart(AuroraMCServerPlayer player) {
        player.getInventory().clear();
        switch (EngineAPI.getActiveMap().getMapData().getString("tool")) {
            case "spade": {
                ItemStack stack = new GUIItem(Material.DIAMOND_SPADE).getItemStack();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getInventory().setItem(0, stack);
                break;
            }
            case "axe": {
                ItemStack stack = new GUIItem(Material.DIAMOND_AXE).getItemStack();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getInventory().setItem(0, stack);
                break;
            }
            case "pickaxe": {
                ItemStack stack = new GUIItem(Material.DIAMOND_PICKAXE).getItemStack();
                stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 50);

                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                stack.setItemMeta(meta);
                player.getInventory().setItem(0, stack);
                break;
            }
        }

    }

    @Override
    public String getUpgradeReward(int i) {
        return "None";
    }
}

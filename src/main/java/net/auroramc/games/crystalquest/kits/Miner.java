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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Miner extends Kit {

    private final static ItemStack is;

    static {
        is = new ItemStack(Material.DIAMOND_PICKAXE);
        is.addEnchantment(Enchantment.DIG_SPEED, 5);
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
    }

    public Miner() {
        super(0, 1, "&3&lMiner", "Some description for Miner", Material.DIAMOND_PICKAXE, -1);
    }

    @Override
    public void onGameStart(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new GUIItem(Material.LEATHER_HELMET).getItem());
        player.getPlayer().getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).getItem());
        player.getPlayer().getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).getItem());
        player.getPlayer().getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).getItem());

        player.getPlayer().getInventory().setItem(0, new GUIItem(Material.STONE_SWORD).getItem());

        player.getPlayer().getInventory().setItem(1, is);
        player.getPlayer().getInventory().setItem(2, new GUIItem(Material.WOOD_AXE).getItem());
        player.getPlayer().getInventory().setItem(8, CrystalQuest.compass);
    }

}
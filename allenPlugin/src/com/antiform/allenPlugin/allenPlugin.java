package com.antiform.allenPlugin;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class allenPlugin
{
	static NamespacedKey notPlaceableBlock = new NamespacedKey(Main.plugin, "notPlaceableBlock");
	static NamespacedKey notStackable = new NamespacedKey(Main.plugin, "notStackable");
	static NamespacedKey customItemBaseName = new NamespacedKey(Main.plugin, "customItemBaseName");
	
	public static HashMap<String, CustomItem> itemMap = new HashMap<String, CustomItem>();
	
	// where we create all the custom items and put it in itemMap
	public static void createCustomItems()
	{
		CustomItem item = new CustomItem(Material.AIR, "null");
		
		// this is mainly a template for items
		item = new CustomItem(Material.STICK, "Debug Item");
		// to get a url, i like to goto minecraft-heads.com and in a head scroll down and under Other > Value: you can get a url
		item.setCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc4OWIzZTI4NjhkNzE2YTkyMWRlYzU5MzJkNTMwYTg5MmY2MDAyMzVmMTg3NzY2YmMwMmQxNDVlZDE2ODY1YiJ9fX0=");
		// this will make it not placeable if it is a block
		item.setCustomValue(notPlaceableBlock, "true");
		// a quick way to set if the item is stackable or not (does not make non-stackable items stackable)
		item.setNotStackable();
		// see list of set rarities in Customitem.java
		item.setRarity("debug");
		// although not nessesary, you can set custom rarity colors
		item.setRarityColor(ChatColor.RED);
		// for all discriptions, lines are seperated by a comma
		item.setDiscription("Just a simple debug item", "for testing and such.", "Score: (Not Working)");
		item.setAbilityName("Debug Power!");
		item.setAbilityDisc("Helps with the", "coding process!");
		// generates the lore from the set variables
		item.updateLore();
		// and finally, puts this item into the list of items. (String itemName, CustomItem item)
		itemMap.put("debugItem", item);

	}
	
	public static CustomItem getItem(String itemName, int count)
	{
		// just tries to give back an item from itemMap
		CustomItem item = new CustomItem(Material.AIR, "null");
		
		if (itemMap.containsKey(itemName)) item = itemMap.get(itemName);
		
		if (item != null)
		{
			item.setAmount(count);
			if (!itemMap.get(itemName).getStackable())
			{				
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().set(allenPlugin.notStackable, PersistentDataType.DOUBLE, Math.random() * 100);
				item.setItemMeta(meta);
			}
		}
		return item;
	}
	
	public static String getCustomItemBaseName(ItemStack item)
	{
		if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(customItemBaseName, PersistentDataType.STRING)) return item.getItemMeta().getPersistentDataContainer().get(customItemBaseName, PersistentDataType.STRING);
		return "null";
	}
	
	// detecting if a players inventory is full is a pain so i copyed this from the internet
	public static boolean invFull(Player p) {          
	    return !Arrays.asList(p.getInventory().getStorageContents()).contains(null); // a quick way to check if a players inventory is full
	}
}
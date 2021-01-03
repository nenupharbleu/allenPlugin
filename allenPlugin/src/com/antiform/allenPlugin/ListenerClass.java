package com.antiform.allenPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ListenerClass implements Listener
{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack handItem = player.getInventory().getItemInMainHand();
		
		if (allenPlugin.getCustomItemBaseName(handItem).contains("Debug Item"))
		{
			if (player.getTargetBlockExact(30) != null) player.getWorld().strikeLightning(player.getTargetBlockExact(30).getLocation());
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.getPlayer() != null)
		{
			Player player = event.getPlayer();
			ItemStack item = event.getItemInHand();
			
			// if an item has this tag, do not let it be placed.
			if (item.getItemMeta().getPersistentDataContainer().has(allenPlugin.notPlaceableBlock, PersistentDataType.STRING))
			{
				event.setCancelled(true);
				player.updateInventory();
			}
		}
	}
	
}
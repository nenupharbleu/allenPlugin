package com.antiform.allenPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("item"))
		{
			// only give and item if a player ran this command.
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				
				// if the player only did /item (we should change this to a inventory gui later)
				if (args.length < 1)
				{
					player.sendMessage(ChatColor.RED + "Not enough Arguments!");
				}
				// if the player did /item <item>
				else if (args.length == 1)
				{
					if (!allenPlugin.getItem(args[0], 1).getType().equals(Material.AIR))
					{
						player.getInventory().addItem(allenPlugin.getItem(args[0], 1));
						player.sendMessage(ChatColor.GREEN + "1 " + ChatColor.LIGHT_PURPLE + args[0] + ChatColor.GOLD + " was added to your Inventory.");
					}
					else player.sendMessage(ChatColor.RED + "That item does not exist!");
				}
				// if the player did /item <item> [count]
				else if (args.length > 1)
				{
					if (!allenPlugin.getItem(args[0], 1).getType().equals(Material.AIR))
					{
						player.getInventory().addItem(allenPlugin.getItem(args[0], Integer.parseInt(args[1])));
						player.sendMessage(ChatColor.GREEN + args[1] + " " + ChatColor.LIGHT_PURPLE + args[0] + ChatColor.GOLD + " was added to your Inventory.");
					}
					else player.sendMessage(ChatColor.RED + "That item does not exist!");
				}
				
				return true;
			}
		}
		
		return false;
	}
	
}
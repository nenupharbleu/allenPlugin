package com.antiform.allenPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletionClass implements TabCompleter
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		
		// here we define the autocomplete stuff for commands.
		// return a list for the list of options in the autocomplete
		if (cmd.getName().equalsIgnoreCase("item"))
		{
			// if we are on the first argument, get all the names of the items in itemMap
			if (args.length == 1)
			{
				List<String> nameList = new ArrayList<String>();
				
				for (int i = 0; i < allenPlugin.itemMap.size(); i++)
				{
					nameList.add((String) allenPlugin.itemMap.keySet().toArray()[i]);
				}
				return nameList;
			}
			// and if we are on the second, just say [Count]
			if (args.length == 2)
			{
				List<String> count = new ArrayList<String>();
				count.add("[Count]");
				return count;
			}
		}
		
		return null;
	}
}
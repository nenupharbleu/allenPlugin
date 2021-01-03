package com.antiform.allenPlugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	// Idea Board:
	/*
	 * 
	 */
	
	public static Plugin plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		this.getServer().getPluginManager().registerEvents(new ListenerClass(), this);
		
		allenPlugin.createCustomItems();
		
		// register commands
		getCommand("item").setExecutor(new CommandClass());
		getCommand("item").setTabCompleter(new TabCompletionClass());
	}
	
	@Override
	public void onDisable()
	{
		plugin = null;
	}
	
}
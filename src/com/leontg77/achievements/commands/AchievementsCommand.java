package com.leontg77.achievements.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.leontg77.achievements.Main;
import com.leontg77.achievements.Utils;
import com.leontg77.achievements.listeners.AchievementListener;

/**
 * Achievements command class.
 * 
 * @author LeonTG77
 */
public class AchievementsCommand implements CommandExecutor, TabCompleter {
	private static final String PERMISSION = "achievements.manage";
	
	private final AchievementListener listener;
	private final Main plugin;
	
	public AchievementsCommand(Main plugin, AchievementListener listener) {
		this.listener = listener;
		this.plugin = plugin;
	}
	
	private boolean enabled = false;
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Main.PREFIX + "Usage: /achievements <info|enable|disable>");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("info")) {
			sender.sendMessage(Main.PREFIX + "Plugin creator: �aLeonTG77");
			sender.sendMessage(Main.PREFIX + "Description:");
			sender.sendMessage("�8� �f" + plugin.getDescription().getDescription());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("enable")) {
			if (!sender.hasPermission(PERMISSION)) {
				sender.sendMessage(ChatColor.RED + "You don't have permission.");
				return true;
			}
			
			if (enabled) {
				sender.sendMessage(ChatColor.RED + "Achievements is already disabled.");
				return true;
			}
			
			Utils.broadcast(Main.PREFIX + "Achievements has been enabled.");
			enabled = true;
			
			Bukkit.getPluginManager().registerEvents(listener, plugin);
			return true;
		}

		if (args[0].equalsIgnoreCase("disable")) {
			if (!sender.hasPermission(PERMISSION)) {
				sender.sendMessage(ChatColor.RED + "You don't have permission.");
				return true;
			}
			
			if (!enabled) {
				sender.sendMessage(ChatColor.RED + "Achievements is not enabled.");
				return true;
			}

			Utils.broadcast(Main.PREFIX + "Achievements has been disabled.");
			enabled = false;
			
			HandlerList.unregisterAll(listener);
			
			for (Player online : Bukkit.getOnlinePlayers()) {
				online.setPlayerListName(null);
			}
			return true;
		}
		
		sender.sendMessage(Main.PREFIX + "Usage: /achievements <info|enable|disable>");
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		final List<String> toReturn = new ArrayList<String>();
		final List<String> list = new ArrayList<String>();
		
		if (args.length != 1) {
			return toReturn;
		}
		
		list.add("info");
		
		if (sender.hasPermission(PERMISSION)) {
			list.add("enable");
			list.add("disable");
		}

		// make sure to only tab complete what starts with what they 
		// typed or everything if they didn't type anything.
		for (String str : list) {
			if (args[0].isEmpty() || str.startsWith(args[0].toLowerCase())) {
				toReturn.add(str);
			}
		}
		
		return toReturn;
	}
}
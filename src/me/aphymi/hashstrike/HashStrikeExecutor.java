package me.aphymi.hashstrike;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class HashStrikeExecutor implements CommandExecutor {
	private static final String name = HashStrike.name;
	private static final String noPerms = name + "§cYou don't have permission for that.";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hashstrike") || cmd.getName().equalsIgnoreCase("hs")) {
			if (args[0].equalsIgnoreCase("enable")) {
				return enableStriking(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("disable")) {
				return disableStriking(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("reload")) {
				return reloadPlugin(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("add")) {
				return add(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("remove")) {
				return remove(sender, cmd, args);
			}
		}
		if (cmd.getName().equalsIgnoreCase("strikelist") || cmd.getName().equalsIgnoreCase("strikewords")) {
			return strikeList(sender, cmd, args);
		}
		return false;
	}
	
	private boolean enableStriking(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.toggle")) {
			sender.sendMessage(noPerms);
			return true;
		}
		HashStrikeListener.isEnabled = true;
		sender.sendMessage(name + "Enabled.");
		return true;
	}
	
	private boolean disableStriking(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.toggle")) {
			sender.sendMessage(noPerms);
			return true;
		}
		HashStrikeListener.isEnabled = false;
		sender.sendMessage(name + "Disabled.");
		return true;
	}
	
	private boolean reloadPlugin(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.reload")) {
			sender.sendMessage(noPerms);
			return true;
		}
		HashStrikeListener.reloadConfig();
		sender.sendMessage(name + "Reloaded.");
		return true;
	}
	
	private boolean add(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.addword")) {
			sender.sendMessage(name + "You don't have permission for that.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(name + "You must specify a word.");
			return true;
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin("HashStrike");
		List<String> words = plugin.getConfig().getStringList("words");
		words.add(args[1]);
		plugin.getConfig().set("words", words);
		plugin.saveConfig();
		sender.sendMessage(name + String.format("'%s' added successfully.", args[1]));
		return true;
	}
	
	private boolean remove(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.removeword")) {
			sender.sendMessage(name + "You don't have permission for that.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(name + "You must specify a word.");
			return true;
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin("HashStrike");
		List<String> words = plugin.getConfig().getStringList("words");
		words.remove(args[1]);
		plugin.getConfig().set("words", words);
		plugin.saveConfig();
		
		sender.sendMessage(name + String.format("'%s' removed successfully.", args[1]));
		return true;
	}
	
	private boolean strikeList(CommandSender sender, Command cmd, String[] args) {
		if (!sender.hasPermission("hashstrike.strikelist")) {
			sender.sendMessage(name + "You don't have permission for that.");
			return true;
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin("HashStrike");
		sender.sendMessage(name + "You will be striked for hashtags and saying any of: " + StringUtils.join(plugin.getConfig().getStringList("words"), ", "));
		return true;
	}
}

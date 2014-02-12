package me.aphymi.hashstrike;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HashStrikeExecutor implements CommandExecutor {
	private static final String name = HashStrike.name;
	private static final String noPerms = name + "§cYou don't have permission for that.";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hashstrike")) {
			if (args[0].equalsIgnoreCase("enable")) {
				return enableStriking(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("disable")) {
				return disableStriking(sender, cmd, args);
			}
			if (args[0].equalsIgnoreCase("reload")) {
				return reloadPlugin(sender, cmd, args);
			}
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
}

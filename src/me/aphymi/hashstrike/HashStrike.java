package me.aphymi.hashstrike;

import org.bukkit.plugin.java.JavaPlugin;

public final class HashStrike extends JavaPlugin {
	public static final String name = "§6[§3HashStrike§6] §7";
	
	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new HashStrikeListener(this), this);
		HashStrikeExecutor executor = new HashStrikeExecutor();
		getCommand("hashstrike").setExecutor(executor);
	}
}

package me.aphymi.hashstrike;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class HashStrikeListener implements Listener {
	
	private Plugin plugin;
	public static boolean isEnabled = true;
	private static Pattern[] regexes;
	private static Pattern[] nostrikeRegexes;
	private static boolean cancelMessages;
	private static int damage;
	private static String[] consoleCommands;
	private static String meta = "hashstrike_warned";
	
	public HashStrikeListener(Plugin plugin) {
		this.plugin = plugin;
		reloadConfig(plugin);
	}
	
	protected static void reloadConfig() {
		reloadConfig(Bukkit.getPluginManager().getPlugin("HashStrike"));
	}
	
	protected static void reloadConfig(Plugin plugin) {
		plugin.reloadConfig();
		Iterator<String> regexIter = plugin.getConfig().getStringList("regexes").iterator();
		ArrayList<Pattern> regexPatterns = new ArrayList<Pattern>();
		while (regexIter.hasNext()) {
			regexPatterns.add(Pattern.compile(regexIter.next(), Pattern.CASE_INSENSITIVE));
		}
		Iterator<String> wordIter = plugin.getConfig().getStringList("words").iterator();
		while (wordIter.hasNext()) {
			StringBuilder sb = new StringBuilder();
			String[] letters = wordIter.next().split("");
			for (String letter: letters) {
				sb.append(letter + "+");
			}
			regexPatterns.add(Pattern.compile("\\b" + sb.toString() + "\\b", Pattern.CASE_INSENSITIVE));
		}
		regexes = regexPatterns.toArray(new Pattern[0]);
		
		Iterator<String> nostrikeIter = plugin.getConfig().getStringList("regexes_nostrike").iterator();
		ArrayList<Pattern> nostrikePatterns = new ArrayList<Pattern>();
		while (nostrikeIter.hasNext()) {
			nostrikePatterns.add(Pattern.compile(nostrikeIter.next(), Pattern.CASE_INSENSITIVE));
		}
		nostrikeRegexes = nostrikePatterns.toArray(new Pattern[0]);
		
		cancelMessages = plugin.getConfig().getBoolean("cancelmessages");
		
		damage = plugin.getConfig().getInt("damage");
		
		consoleCommands = plugin.getConfig().getStringList("strike_commands").toArray(new String[0]);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (!isEnabled) return;
		String message = e.getMessage();
		if (message.equals("")) return;
		
		outer: //Label for outer loop
		for (Pattern pattern: regexes) {
			Matcher match = pattern.matcher(message);
			while (match.find()) {
				for (Pattern nostrike: nostrikeRegexes) {
					if (nostrike.matcher(match.group()).matches()) {
						System.out.println("Unmatched text!");
						continue outer;
					}
				}
				
				Player player = e.getPlayer();
				if (!player.hasMetadata(meta)) {
					player.sendMessage(String.format(
						"§3Oh how clever of you. Please say '%s' again. Every time you do we'll take 25 more marbles. <3 The disallowed words include %s.",
						match.group().trim(),
						StringUtils.join(plugin.getConfig().getStringList("words"), ", ") + " and hashtags."
					));
					player.setMetadata(meta, new FixedMetadataValue(plugin, true));
					return;
				}
				if (player.hasPermission("hashstrike.exempt")) {
					return;
				}
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.damage(damage);
				player.sendMessage(String.format("§3§oBoom! §r§3(You were striked for saying \"%s\")", match.group().trim()));
				if (cancelMessages) {
					e.setCancelled(true);
				}
				for (String command: consoleCommands) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{{USER}}", player.getName()));
				}
				return;
			}
		}
	}
}

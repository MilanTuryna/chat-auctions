package cz.MilanT.drazby.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	private final JavaPlugin plugin;
	private String sender = "";

	public ConfigManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public String getString(String string) {
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(string));
	}

	public void setSender(String nickname) {
		this.sender = nickname;
	}

	public String getPrefix() {
		return this.getString("prefix");
	}
	public String getSeperator() { return this.getString("separator"); }
	public String getCurrency() { return this.getString("currency"); }
	private String getVariablesString(String path) {
		return String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getString(path))
				.replace("%PLAYER%", sender)
				.replace("%PREFIX%", this.getPrefix()));
	}

	public String getMessage(String message) {
		return this.getVariablesString("messages." + message);
	}

	public String getError(String error) {
		return this.getVariablesString("errors." + error);
	}
	
    public FileConfiguration getConfig() {
    	return this.plugin.getConfig();
    }
}

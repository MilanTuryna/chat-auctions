package cz.MilanT.drazby;

import cz.MilanT.drazby.libraries.Vault;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import cz.MilanT.drazby.commands.MainCommand;
import cz.MilanT.drazby.managers.ConfigManager;
import cz.MilanT.drazby.managers.DrazbaManager;

public class Main extends JavaPlugin implements Listener {
	private ConfigManager configManager;
	private DrazbaManager drazbaManager;
	private boolean vaultActive = false;

	@Override
	public void onEnable() {
		Vault vault = new Vault(this);
		this.configManager = new ConfigManager(this);
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);

		this.log("§aDeveloped by MilanT");
		if(vault.setupEconomy()) {
			this.log("§aPlugin na drazby uspesne spusten!");
			this.vaultActive = true;
			this.drazbaManager = new DrazbaManager();
			this.getCommand("drazba").setExecutor(new MainCommand(drazbaManager, configManager, vault));
		} else {
			this.log("§cVault nebyl nalezen, nebo nefunguje spravne, plugin se vypne.");
			this.log("§aPro spravny chod je zapotrebi: §ehttps://github.com/MilkBowl/Vault");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		if(vaultActive) {
			getLogger().info("Plugin na drazby byl vypnut, vsechny itemy byly vraceny");
			Bukkit.getOnlinePlayers().forEach(player -> {
				if(this.drazbaManager.isPlayerAuctioning(player)) {
					this.drazbaManager.endAuction(player, false);
				}
			});
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(this.drazbaManager.isPlayerAuctioning(player)) {
			this.drazbaManager.endAuction(player, false);
			configManager.setSender(player.getDisplayName());
			Bukkit.broadcastMessage(configManager.getMessage("player_quit_auction"));
			configManager.setSender("");
		}
	}
	
	public DrazbaManager getDrazbaManager() {
		return this.drazbaManager;
	}

	public void log(String message) {
		Bukkit.getConsoleSender().sendMessage(this.configManager.getPrefix() + " " + message);
	}
}

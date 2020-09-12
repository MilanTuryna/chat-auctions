package cz.MilanT.drazby.commands;

import java.util.Date;
import java.util.HashMap;

import cz.MilanT.drazby.libraries.Minecraft;
import cz.MilanT.drazby.libraries.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cz.MilanT.drazby.core.Drazba;
import cz.MilanT.drazby.managers.ConfigManager;
import cz.MilanT.drazby.managers.DrazbaManager;

public class MainCommand implements CommandExecutor {
	private final ConfigManager configManager;
	private final DrazbaManager drazbaManager;
	private final Vault vault;

	public MainCommand(DrazbaManager drazbaManager, ConfigManager configManager, Vault vault) {
		this.drazbaManager = drazbaManager;
		this.configManager = configManager;
		this.vault = vault;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String prefix = configManager.getPrefix();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			configManager.setSender(player.getDisplayName());
			Date date = new Date();

			if(args.length == 0) {
				player.sendMessage(configManager.getSeperator());
				player.sendMessage(" ");
				player.sendMessage("  §6Prehled drazebnich prikazu");
				player.sendMessage("  §e/drazba vytvorit §7- Drazba itemu ktery prave drzis v ruce");
				player.sendMessage("  §e/drazba list §7- Seznam momentalnich drazeb");
				player.sendMessage("  §e/drazba prodat §7- Ukonci drazbu a item proda zajemci s nejvyssi cenovou nabidkou");
				player.sendMessage("  §e/drazba konec §7- Konec drazby, item se ti vrati do inventare");
				player.sendMessage("  §e/drazba prihodit <hrac> <castka> - §7Prihodi penezni castku k aukci daneho hrace");
				player.sendMessage(" ");
				player.sendMessage(configManager.getSeperator());
			}

			if(args.length > 0) {
				if (args[0].equalsIgnoreCase("vytvorit")) {
					if (!this.drazbaManager.isPlayerAuctioning(player)) {
						ItemStack itemInHand = player.getInventory().getItemInMainHand();
						if (itemInHand.getType() != Material.AIR) {
							Drazba drazba = new Drazba(player, player.getInventory().getItemInMainHand(), date.getTime());
							ItemStack item = drazba.getItemStack();

							this.drazbaManager.addAuction(player, drazba);

							Bukkit.broadcastMessage(configManager.getSeperator());
							Bukkit.broadcastMessage(" ");
							Bukkit.broadcastMessage("    " + configManager.getMessage("auction_started")
									.replace("%ITEM_AMOUNT%", "" + item.getAmount())
									.replace("%ITEM_NAME%", Minecraft.getItemFullName(item))
							);
							Bukkit.broadcastMessage("    " + configManager.getMessage("usage_plus"));
							Bukkit.broadcastMessage(" ");
							Bukkit.broadcastMessage(configManager.getSeperator());
						} else {
							player.sendMessage(configManager.getError("no_item"));
						}
					} else {
						player.sendMessage(configManager.getError("same_time_auction"));
					}
				} else if (args[0].equalsIgnoreCase("prodat")) {
					if (this.drazbaManager.isPlayerAuctioning(player)) {
						Drazba drazba = this.drazbaManager.getDrazba(player);
						Player lastTaker = drazba.getLastTaker();

						if (lastTaker != null) {
							if (this.vault.getEcon().getBalance(lastTaker) > drazba.getPrice()) {
								this.vault.getEcon().withdrawPlayer(lastTaker, drazba.getPrice());
								this.vault.getEcon().depositPlayer(player, drazba.getPrice());
								this.drazbaManager.sellAuction(drazba);

								ItemStack item = drazba.getItemStack();

								player.sendMessage(configManager.getMessage("auction_selled_owner")
										.replace("%ITEM_AMOUNT%", "" + item.getAmount())
										.replace("%ITEM_NAME%", Minecraft.getItemFullName(item))
										.replace("%LAST_TAKER%", lastTaker.getDisplayName()));
								lastTaker.sendMessage(configManager.getMessage("auction_selled_lastTaker"));
							} else {
								player.sendMessage(configManager.getMessage("player_taker_no_money"));
								lastTaker.sendMessage(configManager.getMessage("taker_no_money"));
							}
						} else {
							player.sendMessage(configManager.getMessage("no_taker"));
						}

					} else {
						player.sendMessage(configManager.getError("no_auction_sell"));
					}
				} else if (args[0].equalsIgnoreCase("konec")) {
					if (this.drazbaManager.isPlayerAuctioning(player)) {
						this.drazbaManager.endAuction(player, false);
						Bukkit.broadcastMessage(configManager.getMessage("auction_ended_broadcast"));
						player.sendMessage(configManager.getMessage("auction_ended_pm"));
					} else {
						player.sendMessage(configManager.getError("no_auction_end"));
					}
				} else if (args[0].equalsIgnoreCase("prihodit")) {
					if (args.length == 1 || args.length == 2) {
						player.sendMessage(prefix + configManager.getError("bad_argument"));
					} else if (args.length == 3) {
						Player auctionOwner = Bukkit.getPlayer(args[1]);
						if (auctionOwner != null) {
							if (this.drazbaManager.isPlayerAuctioning(auctionOwner)) {
								int price;
								try {
									price = Integer.parseInt(args[2]);
								} catch (NumberFormatException exception) {
									player.sendMessage(configManager.getError("sum_not_number"));
									return true;
								}
								if(auctionOwner != player) {
									if (this.vault.getEcon().getBalance(player) > price) {
										Drazba drazba = this.drazbaManager.getDrazba(auctionOwner);
										if (price > drazba.getPrice()) {
											this.drazbaManager.getDrazba(auctionOwner).changeLastTaker(player, price);
											player.sendMessage(configManager.getMessage("success_plus")
													.replace("%ITEM_PRICE%", price + ""));
										} else {
											player.sendMessage(configManager.getMessage("needed_more_plus").replace("%ITEM_PRICE%", price
													+ ""));
										}
									} else {
										player.sendMessage(configManager.getError("no_money"));
									}
								} else {
									player.sendMessage(configManager.getError("custom_auction_plus"));
								}
							} else {
								player.sendMessage(configManager.getError("player_no_auction"));
							}
						} else {
							player.sendMessage(configManager.getError("player_offline"));
						}
					}
				} else if(args[0].equalsIgnoreCase("list")) {
					// list_auctions
					player.sendMessage(configManager.getMessage("list_auctions"));
					player.sendMessage(configManager.getSeperator());
					player.sendMessage(" ");
					HashMap<Player, Drazba> drazby = this.drazbaManager.getHashMap();
					if (!drazby.isEmpty()) {
						drazby.forEach((key, value) -> {
							ItemStack item = value.getItemStack();
							player.sendMessage("    " + configManager.getMessage("list_example")
									.replace("%OWNER%", key.getDisplayName())
									.replace("%ITEM_PRICE%", value.getPrice() + "")
									.replace("%ITEM_NAME%", Minecraft.getItemFullName(item))
									.replace("%ITEM_AMOUNT%", item.getAmount() + "")
									.replace("%CURRENCY%", configManager.getCurrency()));
						});
					} else {
						// no auction
						player.sendMessage(configManager.getMessage("no_auction"));
					}
					player.sendMessage(" ");
					player.sendMessage(configManager.getSeperator());
				} else {
					//bad argument
					player.sendMessage(configManager.getError("bad_argument"));
				}
			}
		} else {
			sender.sendMessage(configManager.getError("console_error"));
		}

		configManager.setSender("");
		return true;
	}
}
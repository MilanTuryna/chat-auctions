package cz.MilanT.drazby.core;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Drazba {
	private final Player owner;
	private final ItemStack item;
	private final long time;

	private Player lastTaker;
	private double price;
	
	public Drazba(Player owner, ItemStack item, long time) {
		this.owner = owner;
		this.item = item;
		this.time = time;
		this.lastTaker = null;
	}

	public void changeLastTaker(Player player, double price) {
		this.lastTaker = player;
		this.price = price;
	}


	public void removeItem() {
		this.owner.getInventory().removeItem(item);
	}
	
	public void backItem() {
		this.owner.getInventory().addItem(this.item);
	}
	
	public void sellItem() {
		PlayerInventory lastTakerInventory = this.lastTaker.getInventory();
		lastTakerInventory.addItem(this.item);
	}
	
	public long getTime() {
		return this.time;
	}
	public ItemStack getItemStack() {
		return this.item;
	}
	public Player getPlayer() {
		return this.owner;
	}
	public Player getLastTaker() { return this.lastTaker; }
	public double getPrice() { return this.price; }
}

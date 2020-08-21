package cz.MilanT.drazby.managers;

import java.util.HashMap;
import org.bukkit.entity.Player;
import cz.MilanT.drazby.core.Drazba;

public class DrazbaManager {
	private final HashMap<Player, Drazba> database;
	
	public DrazbaManager() {
		this.database = new HashMap<Player, Drazba>();
	}
	
	public boolean isPlayerAuctioning(Player player) {
		return this.database.containsKey(player);
	}

	public Drazba getDrazba(Player player) {
		return this.database.get(player);
	}
	
	public void addAuction(Player player, Drazba drazba) { 
		this.database.put(player, drazba);
		drazba.removeItem();
	}
	
	public void endAuction(Player player, boolean selled) {
		if(!selled) {
			this.getDrazba(player).backItem();
		}
		
		this.database.remove(player);
	}
	
	public void sellAuction(Drazba drazba) {
		drazba.sellItem();
		this.endAuction(drazba.getPlayer(), true);
	}
	
	
	public HashMap<Player, Drazba> getHashMap() {
		return this.database;
	}
}

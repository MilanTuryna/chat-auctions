package cz.MilanT.drazby.libraries;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Vault {
    private final JavaPlugin plugin;
    private Economy econ;

    public Vault(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if(this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon() {
        return econ;
    }
}

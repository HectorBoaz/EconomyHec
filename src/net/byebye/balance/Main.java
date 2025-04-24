package net.byebye.balance;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {

    public static Main m;
    public static FileConfiguration cf;
    public static BukkitScheduler sh;
    public static PluginManager pm;
    private Economy economy;

    @Override
    public void onLoad() {
        m = this;
        cf = getConfig();
        sh = Bukkit.getScheduler();
        pm = Bukkit.getPluginManager();
    }

    @Override
    public void onEnable() {
        pm.registerEvents(new Events(), this);

        // Comandos de Saldo (já existentes)
        m.getCommand("pagar").setExecutor(new Commands());
        m.getCommand("saldo").setExecutor(new Commands());
        m.getCommand("versaldo").setExecutor(new Commands());
        m.getCommand("addsaldo").setExecutor(new CommandsAdmin());
        m.getCommand("setsaldo").setExecutor(new CommandsAdmin());

        // Comandos de Cash (novos)
        m.getCommand("cash").setExecutor(new Commands());
        m.getCommand("vercash").setExecutor(new Commands());
        m.getCommand("enviarcash").setExecutor(new Commands());
        m.getCommand("addcash").setExecutor(new CommandsAdmin());
        m.getCommand("setcash").setExecutor(new CommandsAdmin());
        m.getCommand("removecash").setExecutor(new CommandsAdmin());

        this.economy = new Economy();
        BalanceAPI.init(this.economy);

        // Registra sua economia no Vault
        getServer().getServicesManager().register(
                net.milkbowl.vault.economy.Economy.class,
                new VaultEconomyImpl(this.economy),
                this,
                org.bukkit.plugin.ServicePriority.Normal
        );
    }

    public Economy getEconomy() {
        return this.economy;
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(new Events());
    }
}
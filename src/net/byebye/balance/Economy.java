package net.byebye.balance;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Economy {

    public static final HashMap<UUID, Double> playerSaldos = new HashMap<>();
    public static final HashMap<UUID, Double> playerCash = new HashMap<>();
    private final File balancesFile = new File(Main.m.getDataFolder(), "balances.yml");
    private final FileConfiguration balancesConfig = YamlConfiguration.loadConfiguration(balancesFile);
    private final File cashFile = new File(Main.m.getDataFolder(), "cash.yml");
    private final FileConfiguration cashConfig = YamlConfiguration.loadConfiguration(cashFile);

    public Economy() {
        loadSaldos();
        loadCash();
    }

    // Métodos para o sistema de Saldo (já existentes)
    public void addSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerSaldos.put(uuid, getSaldo(target) + amount);
        saveSaldo(target);
    }

    public void removeSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        double newBalance = Math.max(0, getSaldo(target) - amount);
        playerSaldos.put(uuid, newBalance);
        saveSaldo(target);
    }

    public void setSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerSaldos.put(uuid, amount);
        saveSaldo(target);
    }

    public double getSaldo(OfflinePlayer target) {
        UUID uuid = target.getUniqueId();
        return playerSaldos.getOrDefault(uuid, 0.0);
    }

    public void saveSaldo(OfflinePlayer player) {
        String key = player.getName() + "." + player.getUniqueId();
        balancesConfig.set(key, getSaldo(player));
        try {
            balancesConfig.save(balancesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSaldos() {
        for (String playerName : balancesConfig.getKeys(false)) {
            ConfigurationSection section = balancesConfig.getConfigurationSection(playerName);
            if (section == null) continue;

            for (String uuidStr : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    double saldo = section.getDouble(uuidStr);
                    playerSaldos.put(uuid, saldo);
                } catch (IllegalArgumentException e) {
                    Main.m.getLogger().warning("UUID inválido em balances.yml: " + uuidStr);
                }
            }
        }
    }

    // Métodos para o sistema de Cash (novos)
    public void addCash(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerCash.put(uuid, getCash(target) + amount);
        saveCash(target);
    }

    public void removeCash(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        double newCash = Math.max(0, getCash(target) - amount);
        playerCash.put(uuid, newCash);
        saveCash(target);
    }

    public void setCash(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerCash.put(uuid, amount);
        saveCash(target);
    }

    public double getCash(OfflinePlayer target) {
        UUID uuid = target.getUniqueId();
        return playerCash.getOrDefault(uuid, 0.0);
    }

    public void saveCash(OfflinePlayer player) {
        String key = player.getName() + "." + player.getUniqueId();
        cashConfig.set(key, getCash(player));
        try {
            cashConfig.save(cashFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCash() {
        if (!cashFile.exists()) {
            try {
                cashFile.getParentFile().mkdirs();
                cashFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String playerName : cashConfig.getKeys(false)) {
            ConfigurationSection section = cashConfig.getConfigurationSection(playerName);
            if (section == null) continue;

            for (String uuidStr : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    double cash = section.getDouble(uuidStr);
                    playerCash.put(uuid, cash);
                } catch (IllegalArgumentException e) {
                    Main.m.getLogger().warning("UUID inválido em cash.yml: " + uuidStr);
                }
            }
        }
    }

    public boolean hasCash(OfflinePlayer target, double amount) {
        return getCash(target) >= amount;
    }
}
package net.byebye.balance;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Economy economy = new Economy();

        // Comandos administrativos de Saldo (já existentes)
        if (command.getName().equalsIgnoreCase("addsaldo")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse /addsaldo <player> <valor>.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);
            economy.addSaldo(target, valor);
            sender.sendMessage("§eO valor §aR$" + valor + " §efoi adicionado a conta de §a" + target.getName() + ".");
            return true;
        }

        if (command.getName().equalsIgnoreCase("setsaldo")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse /setsaldo <player> <valor>.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);
            economy.setSaldo(target, valor);
            sender.sendMessage("§eO saldo de §a" + target.getName() + " §efoi setado para §aR$" + valor);
            return true;
        }

        // Comandos administrativos de Cash (novos)
        if (command.getName().equalsIgnoreCase("addcash")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse /addcash <player> <valor>.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);
            economy.addCash(target, valor);
            sender.sendMessage("§eO valor §6" + valor + " Cash §efoi adicionado a conta de §e" + target.getName() + ".");
            if (target.isOnline()) {
                target.getPlayer().sendMessage("§eVocê recebeu §6" + valor + " Cash§e.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("setcash")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse /setcash <player> <valor>.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);
            economy.setCash(target, valor);
            sender.sendMessage("§eO cash de §e" + target.getName() + " §efoi setado para §6" + valor);
            if (target.isOnline()) {
                target.getPlayer().sendMessage("§eSeu cash foi setado para §6" + valor + "§e.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("removecash")) {
            if (args.length < 2) {
                sender.sendMessage("§cUse /removecash <player> <valor>.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);
            economy.removeCash(target, valor);
            sender.sendMessage("§eO valor §6" + valor + " Cash §efoi removido da conta de §e" + target.getName() + ".");
            if (target.isOnline()) {
                target.getPlayer().sendMessage("§cVocê perdeu §6" + valor + " Cash§c.");
            }
            return true;
        }

        return false;
    }
}
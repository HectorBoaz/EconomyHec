package net.byebye.balance;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Events implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("Economy") &&
                !e.getView().getTitle().contains("Economia") &&
                !e.getView().getTitle().contains("Pagamento") &&
                !e.getView().getTitle().contains("Cash")) {
            return; // Não é inventário do sistema, ignorar
        }

        Player player = (Player) e.getWhoClicked();
        Economy economy = new Economy();
        Location loc = player.getLocation();
        e.setCancelled(true);

        // Tela do /pagar (já existente)
        if (e.getView().getTitle().equalsIgnoreCase("§eConfirmação de Pagamento")) {
            if (e.getCurrentItem() == null) return;

            OfflinePlayer target = Bukkit.getOfflinePlayer(Commands.pagamentoConfirmacao.get(player.getUniqueId()));
            Double valor = Commands.pagamentoValor.get(player.getUniqueId());
            Double saldoplayer = economy.getSaldo(player);

            if (e.getCurrentItem().getType() == Material.LIME_WOOL) {
                player.closeInventory();
                if (saldoplayer < valor) {
                    player.sendMessage("§cVocê não dinheiro suficiente.");
                    player.playSound(loc, Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
                economy.addSaldo(target, valor);
                economy.removeSaldo(player, valor);
                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                player.sendMessage("§eVocê pagou §aR$" + valor + " §ea §d" + target.getName());
                if (target.isOnline()) {
                    target.getPlayer().sendMessage("§eVocê recebeu §aR$" + valor + " §ede §d" + player.getName());
                }
            } else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                player.sendMessage("§cVocê cancelou o pagamento.");
                player.closeInventory();
            }
        }

        // Tela do /enviarcash (novo)
        if (e.getView().getTitle().equalsIgnoreCase("§6Confirmação de Cash")) {
            if (e.getCurrentItem() == null) return;

            OfflinePlayer target = Bukkit.getOfflinePlayer(Commands.cashConfirmacao.get(player.getUniqueId()));
            Double valor = Commands.cashValor.get(player.getUniqueId());
            Double cashplayer = economy.getCash(player);

            if (e.getCurrentItem().getType() == Material.LIME_WOOL) {
                player.closeInventory();
                if (cashplayer < valor) {
                    player.sendMessage("§cVocê não tem cash suficiente.");
                    player.playSound(loc, Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
                economy.addCash(target, valor);
                economy.removeCash(player, valor);
                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                player.sendMessage("§eVocê enviou §6" + valor + " Cash §epara §e" + target.getName());
                if (target.isOnline()) {
                    target.getPlayer().sendMessage("§eVocê recebeu §6" + valor + " Cash §ede §e" + player.getName());
                    target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            } else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                player.sendMessage("§cVocê cancelou o envio de cash.");
                player.closeInventory();
            }
        }
    }
}
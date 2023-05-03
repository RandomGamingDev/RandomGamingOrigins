package me.randomgamingdev.randomgamingorigins.tasks;

import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class OpenInvTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;
    public Player player;
    public Inventory inv;

    public OpenInvTask(RandomGamingOrigins plugin, Player player, Inventory inv) {
        this.plugin = plugin;
        this.player = player;
        this.inv = inv;
    }

    @Override
    public void run() {
        player.openInventory(inv);
    }

}

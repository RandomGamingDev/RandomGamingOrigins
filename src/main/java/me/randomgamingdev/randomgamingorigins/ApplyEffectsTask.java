package me.randomgamingdev.randomgamingorigins;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ApplyEffectsTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;
    public Player player;
    public Origin origin;

    public ApplyEffectsTask(RandomGamingOrigins plugin, Player player, Origin origin) {
        this.plugin = plugin;
        this.player = player;
        this.origin = origin;
    }

    @Override
    public void run() {
        Origins.ApplyOriginEffects(player, origin);
    }

}
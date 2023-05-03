package me.randomgamingdev.randomgamingorigins.tasks;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import org.bukkit.entity.Player;
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
        OriginManager.ApplyOriginEffects(player, origin);
    }

}
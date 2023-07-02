package me.randomgamingdev.randomgamingorigins.core.tasks;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.playersData;

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
        final PlayerData playerData = playersData.get(player.getUniqueId());
        playerData.origin.origin.applyEffects(player, playerData);
    }
}
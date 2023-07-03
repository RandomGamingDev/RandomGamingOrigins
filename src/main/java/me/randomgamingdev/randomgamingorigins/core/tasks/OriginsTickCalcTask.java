package me.randomgamingdev.randomgamingorigins.core.tasks;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.playersData;

public class OriginsTickCalcTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;

    public OriginsTickCalcTask(RandomGamingOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Origin origin : Origin.values())
            origin.origin.perTick();
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = OriginManager.GetPlayerData(player);
            playerData.origin.origin.perPlayerPerTick(player, playerData);
        }
    }
}
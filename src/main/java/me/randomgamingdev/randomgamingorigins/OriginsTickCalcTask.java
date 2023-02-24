package me.randomgamingdev.randomgamingorigins;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OriginsTickCalcTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;

    OriginsTickCalcTask(RandomGamingOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers())
            switch (Origins.playersData.get(player.getUniqueId()).origin) {
                case Merling:
                    if (player.isInWater()) {
                        player.setRemainingAir(10 * 30);
                        break;
                    }
                    int remainingAir = player.getRemainingAir();
                    if (remainingAir >= 0 && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                        player.setRemainingAir(remainingAir - 6);
                    break;
            }
    }
}
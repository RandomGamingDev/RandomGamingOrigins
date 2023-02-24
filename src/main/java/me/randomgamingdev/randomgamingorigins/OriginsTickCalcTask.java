package me.randomgamingdev.randomgamingorigins;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
                        player.setRemainingAir(player.getRemainingAir() + 5);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 15, true, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2, 1, true, false));
                        break;
                    }
                    int remainingAir = player.getRemainingAir();
                    if (remainingAir >= 0 && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                        player.setRemainingAir(remainingAir - 6);
                    break;
            }
    }
}
package me.randomgamingdev.randomgamingorigins.tasks;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OriginsTickCalcTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;

    public OriginsTickCalcTask(RandomGamingOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers())
            switch (OriginManager.playersData.get(player.getUniqueId()).origin) {
                case Merling:
                    int remainingAir = player.getRemainingAir();
                    if (player.isInWater()) {
                        if (remainingAir < 300)
                            player.setRemainingAir(remainingAir + 5);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 14, true, false));
                        break;
                    }
                    if (remainingAir >= 0 && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                        player.setRemainingAir(remainingAir - 6);
                    break;
                case Elytrian:
                    if (player.isGliding())
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 0, true, false));
                    break;
                case Piglin:
                    Material item = player.getInventory().getItemInMainHand().getType();
                    for (Material tool : OriginManager.goldenTools)
                        if (item == tool)
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 1, true, false));
                    break;
            }
    }
}
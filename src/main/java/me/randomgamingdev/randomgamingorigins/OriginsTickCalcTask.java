package me.randomgamingdev.randomgamingorigins;

import org.bukkit.attribute.Attribute;
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
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 14, true, false));
                        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1);
                        break;
                    }
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                    int remainingAir = player.getRemainingAir();
                    if (remainingAir >= 0 && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                        player.setRemainingAir(remainingAir - 6);
                    break;
                case Elytrian:
                    if (player.isGliding())
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 1, true, false));
                    break;
            }
    }
}
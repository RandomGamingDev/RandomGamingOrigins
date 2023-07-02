package me.randomgamingdev.randomgamingorigins.core.tasks;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OriginsSecondCalcTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;

    public OriginsSecondCalcTask(RandomGamingOrigins plugin) {
        this.plugin = plugin;
    }

    public static void TickDown(Player player, PlayerData playerData) {
        if (playerData.abilityTimer <= 0)
            return;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
            String.format("%ds before you can use your ability again", playerData.abilityTimer)));
        playerData.abilityTimer--;
    }

    public static void WaterDamage(Player player, PlayerData playerData, double damage) {
        if (!player.isInWater())
            return;
        switch (playerData.origin) {
            case Blazeborn:
                playerData.deathCause = String.format("%s burned out", player.getName());
                break;
            default:
                playerData.deathCause = String.format("%s tried to swim in water", player.getName());
                break;
        }
        player.damage(damage);
    }

    public static void RainDamage(Player player, PlayerData playerData, double damage) {
        if (player.getInventory().getHelmet() != null)
            return;
        World world = player.getWorld();
        if (!world.hasStorm() && !world.isThundering())
            return;
        Location location = player.getLocation();
        double temperature = location.getBlock().getTemperature();
        if (temperature < 0.15 || temperature > 0.95)
            return;
        int blockLocation = world.getHighestBlockYAt(location);
        if (blockLocation > player.getLocation().getY())
            return;
        switch (playerData.origin) {
            case Blazeborn:
                playerData.deathCause = String.format("%s burned out", player.getName());
                break;
            default:
                playerData.deathCause = String.format("%s died of rain", player.getName());
                break;
        }
        player.damage(damage);
    }

    @Override
    public void run() {
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = OriginManager.playersData.get(player.getUniqueId());
            playerData.origin.origin.perSecond(player, playerData);
            TickDown(player, playerData);
        }
    }
}

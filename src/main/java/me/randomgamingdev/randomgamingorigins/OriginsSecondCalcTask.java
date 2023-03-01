package me.randomgamingdev.randomgamingorigins;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OriginsSecondCalcTask extends BukkitRunnable {
    private final RandomGamingOrigins plugin;

    OriginsSecondCalcTask(RandomGamingOrigins plugin) {
        this.plugin = plugin;
    }

    public void TickDown(Player player, PlayerData playerData) {
        if (playerData.abilityTimer <= 0)
            return;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
            String.format("%ds before you can use your ability again", playerData.abilityTimer)));
        playerData.abilityTimer--;
    }

    public void WaterDamage(Player player, PlayerData playerData, double damage) {
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

    public void RainDamage(Player player, PlayerData playerData, double damage) {
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
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = Origins.playersData.get(player.getUniqueId());
            TickDown(player, playerData);
            switch (playerData.origin) {
                case Blazeborn:
                    if (player.getFireTicks() > 0)
                        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
                    else
                        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                    WaterDamage(player, playerData, 2);
                    RainDamage(player, playerData, 1);
                    break;
                case Merling:
                    if (player.getRemainingAir() > 0)
                        break;
                    playerData.deathCause = String.format("%s drowned on land", player.getName());
                    player.damage(1);
                    break;
                case Enderian:
                    WaterDamage(player, playerData, 1);
                    RainDamage(player, playerData, 1);
                    break;
                case Phantom:
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20, 0, true, false));
                    break;
                case Evoker:
                    GameMode gameMode = player.getGameMode();
                    if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE)
                        break;
                    for (Entity entity : player.getNearbyEntities(32, 32, 32))
                        if (entity.getType().equals(EntityType.IRON_GOLEM))
                            ((IronGolem)entity).setTarget(player);
                    break;
            }
        }
    }
}

package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class LunarOrigin extends NullOrigin {
    public LunarOrigin() {
        this.origin = Origin.Lunar;
        this.name = "Lunar";
        this.dispItem = createGuiItem(Material.END_STONE, true,
                "§r§fLunar",
                "§7- Crescent Worship: You get 1 minute of",
                "§7regeneration and speed 1 by pressing offhand swap ",
                "§7key every 2 minutes",
                "§7- Bloodthirsty: boosted attack at night",
                "§7- Bouncing Moonlight: you glow at night",
                "§7- Blinding Sun: your abilities don't work during the day",
                "§7- Conquering Sun: decreased speed during the day");
        this.maxHealth = 10 * 2;
    }

    public boolean isDay(Player player) {
        long time = player.getWorld().getTime();
        return time > 0 && time < 12300;
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        if (isDay(player))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 0, true, false));
        else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0, true, false));
        }
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;
        event.setCancelled(true);

        if (isDay(player))
            player.sendMessage("You can't use this ability during the day time");
        else {
            playerData.abilityTimer = 120;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 0, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 0, true, true));
        }
    }

}
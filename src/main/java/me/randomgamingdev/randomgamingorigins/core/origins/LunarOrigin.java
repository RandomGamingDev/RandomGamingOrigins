package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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
                "§7- Crescent Worship: You get Regeneration and Speed 1 by pressing Offhand Swap Key.",
                "§7- Bloodthirsty: Boosted attack at night",
                "§7- Bouncing Moonlight: You glow at night",
                "§7- Blinding Sun: Abilities don't work during the day",
                "§7- Conquering Sun: Decreased speed during the day");
        this.maxHealth = 10 * 2;
    }

    public boolean day() {
        Server server = getServer();
        long time = server.getWorld("world").getTime();

        if(time > 0 && time < 12542) {
            return true;
        } else {
            return false;
        }
    }

    if(day()) {
        Player player = event.getPlayer();

        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));
    } else {
        Player player = event.getPlayer();

        player.removePotionEffect(PotionEffectType.SLOW);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false), (PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, true, false));
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        event.setCancelled(true);
        if (day()) {
            player.sendMessage(String.format("Can't use in day time", origin.origin.name));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 0, true, true));

        }

}
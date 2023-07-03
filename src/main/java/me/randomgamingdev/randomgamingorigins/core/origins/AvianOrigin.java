package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class AvianOrigin extends NullOrigin {
    public AvianOrigin() {
        this.origin = Origin.Avian;
        this.name = "Avian";
        this.dispItem = createGuiItem(Material.FEATHER, true,
                        "§r§fAvian",
                        "§7- Tailwind: You have speed 1",
                        "§7- Featherweight: You have feather falling 1",
                        "§7- Flight: You can boost yourself in the direction",
                        "§7you're looking every 5 seconds",
                        "§7- Vegetarian: You can't eat meat");
        this.initEffects = new Object[]{ new Pair(PotionEffectType.SPEED, 0), new Pair(PotionEffectType.SLOW_FALLING, 0) };
        this.maxHealth = 10 * 2;
    }

    @Override
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        Material itemType = event.getItem().getType();

        for (Material meat : OriginManager.meats) {
            if (itemType != meat)
                continue;
            player.sendMessage(String.format("%s's can't eat meat!", this.name));
            event.setCancelled(true);
            break;
        }
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 5;
        Player player = event.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(3));
    }
}

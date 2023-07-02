package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class PhantomOrigin extends NullOrigin {
    public PhantomOrigin() {
        this.origin = Origin.Phantom;
        this.name = "Phantom";
        this.dispItem = createGuiItem(Material.PHANTOM_MEMBRANE, true,
                            "§r§fPhantom",
                            "§7- Phantom Form: Press your offhand",
                            "§7swap key to turn invisible",
                            "§7- Fragile: You have 3 less hearts than normal players");
        this.initEffects = new Object[]{};
        this.maxHealth = 7 * 2;
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        event.setCancelled(true);
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
    }

    @Override
    public void perSecond(Player player, PlayerData playerData) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20, 0, true, false));
    }
}

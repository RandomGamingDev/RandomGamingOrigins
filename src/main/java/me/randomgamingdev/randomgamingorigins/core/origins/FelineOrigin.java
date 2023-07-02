package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class FelineOrigin extends NullOrigin {
    public FelineOrigin() {
        this.origin = Origin.Feline;
        this.name = "Feline";
        this.dispItem = createGuiItem(Material.ORANGE_WOOL, true,
            "§r§fFeline",
            "§7- Acrobatics: you take no fall damage",
            "§7- Strong Ankles: You have jump boost 1",
            "§7- Nine Lives: You have 9 hearts total");
        this.initEffects = new Object[]{ new Pair(PotionEffectType.JUMP, 0) };
        this.maxHealth = 9 * 2;
}

    @Override
    public void onPlayerDamageEvent(EntityDamageEvent event, PlayerData playerData) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);
    }
}
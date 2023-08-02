package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class SerpentOrigin extends NullOrigin {
    public SerpentOrigin() {
        this.origin = Origin.Serpent;
        this.name = "Serpent";
        this.dispItem = createGuiItem(Material.POINTED_DRIPSTONE, true,
                "§r§fSerpent",
                "§7- Poisonous Bite: Attacking other players causes has a 25%",
                "§7chance of inflicting poison damage that lasts for 4 seconds",
                "§7- Swift Slithering: You have speed 1",
                "§7- Fangs: You have strength 1",
                "§7- Carnivorous: Can only eat meat",
                "§7- Weak Creature: You have 9 hearts");
        this.initEffects = new Object[]{new Pair(PotionEffectType.SPEED, 0), new Pair(PotionEffectType.INCREASE_DAMAGE, 0)};
        this.maxHealth = 9 * 2;
    }

    @Override
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        Material itemType = event.getItem().getType();

        for (Material meat : OriginManager.meats)
            if (itemType == meat)
                return;
        player.sendMessage(String.format("%s's can only eat meat!", this.name));
        event.setCancelled(true);
    }

    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;

        Player player = (Player)entity;
        if (Math.floor(Math.random() * 4) == 0)
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 0));
    }
}

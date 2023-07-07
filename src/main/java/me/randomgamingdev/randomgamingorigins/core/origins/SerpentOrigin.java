package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
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
                "§7- Poisonous Bite: Attacking other players causes poison damage",
                "§7- Fangs: Have extra attack damage",
                "§7- Swift Slithering: Slightly Faster",
                "§7- Carnivorous: Can only eat meat",
                "§7- Weak Creature: Has 1 less heart");
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


    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 60;
        event.setCancelled(true);

        Player player = event.getPlayer();
        player.setVelocity(player.getLocation().getDirection().multiply(2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 80, 0, true, false));
    }

    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
    }
}

package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class FrogOrigin extends NullOrigin {
    public FrogOrigin() {
        this.origin = Origin.Frog;
        this.name = "Frog";
        this.dispItem = createGuiItem(Material.SLIME_BALL, true,
                        "§r§fFrog",
                        "§7- Amphibious: You can breathe both in and out of water",
                        "§7- Webbed Feet: You have dolphins grace",
                        "§7- Hippity-Hoppity: You have jump boost 2",
                        "§7- Small Heart: You have 3 less hearts than a normal player",
                        "§7- Unwieldy: You cannot use shields",
                        "§7- Leap of Faith: Press your offhand swap key to get launched into the air",
                        "§7which allows you to deal more damage");
        this.initEffects = new Object[]{
                new Pair(PotionEffectType.DOLPHINS_GRACE, 0),
                new Pair(PotionEffectType.WATER_BREATHING, 0),
                new Pair(PotionEffectType.JUMP, 1)
        };
        this.maxHealth = 7 * 2;
    }

    @Override
    public void onPlayerDamageEvent(EntityDamageEvent event, PlayerData playerData) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        if (item == null)
            return;
        final Material itemType = item.getType();

        final PlayerInventory inventory = player.getInventory();
        final ItemStack handItem = inventory.getItemInMainHand();

        if (!itemType.equals(Material.SHIELD))
            return;

        player.sendMessage(String.format("%s's can't use shields!", origin.origin.name));

        Location location = player.getLocation();
        World world = player.getWorld();
        if (handItem.getType().equals(Material.SHIELD))
            inventory.setItemInMainHand(null);
        else
            inventory.setItemInOffHand(null);
        world.dropItem(location, item);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        final Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 60;
        event.setCancelled(true);
        player.setVelocity(player.getVelocity().setY(2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 2, true, false));
    }
}

package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class FoxOrigin extends NullOrigin {
    public FoxOrigin() {
        this.origin = Origin.Fox;
        this.name = "Fox";
        this.dispItem = createGuiItem(Material.SWEET_BERRIES, true,
                        "§r§fFox",
                        "§7- Pounce: Press your offhand swap key to get launched in the air",
                        "§7while falling you deal a lot of damage",
                        "§7- Light Body: You have permanent jump boost 1 and speed 1",
                        "§7- Mighty Mouth: By pressing your offhand swap key and sneaking, thus",
                        "§7allowing you to open up a second inventory with 9 slots that's preserved",
                        "§7even through death",
                        "§7- Acrobatics: You never take fall damage",
                        "§7- Nocturnal: You have permanent night vision",
                        "§7- Berry Craver: You gain 1 extra hunger point from sweat berries",
                        "§7- Fast Metabolism: You loose hunger faster",
                        "§7- Unwieldy: You cannot use shields",
                        "§7- Smaller Heart: You have 6 max hearts");
        this.initEffects = new Object[]{
                                new Pair(PotionEffectType.HUNGER, 0),
                                new Pair(PotionEffectType.NIGHT_VISION, 0),
                                new Pair(PotionEffectType.JUMP, 0),
                                new Pair(PotionEffectType.SPEED, 1)
                            };
        this.maxHealth = 6 * 2;
    }

    @Override
    public void applyInv(Player player, PlayerData playerData) {
        playerData.pouch = Bukkit.createInventory(null, 1 * 9,
                String.format("§2Your %s Inventory", this.name));
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material itemType = item.getType();
        PlayerInventory inventory = player.getInventory();
        ItemStack handItem = inventory.getItemInMainHand();

        if (!itemType.equals(Material.SHIELD))
            return;
        player.sendMessage(String.format("%s's can't use shields!", this.name));

        Location location = player.getLocation();
        World world = player.getWorld();
        if (handItem.getType().equals(Material.SHIELD))
            inventory.setItemInMainHand(null);
        else
            inventory.setItemInOffHand(null);
        world.dropItem(location, item);
    }

    @Override
    public void onPlayerDamageEvent(EntityDamageEvent event, PlayerData playerData) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (
                damageCause == EntityDamageEvent.DamageCause.FALL ||
                damageCause == EntityDamageEvent.DamageCause.THORNS
            )
                event.setCancelled(true);
    }

    @Override
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material itemType = item.getType();

        if (itemType.equals(Material.SWEET_BERRIES))
            player.setFoodLevel(player.getFoodLevel() + 1);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            event.setCancelled(true);
            player.openInventory(playerData.pouch);
            return;
        }
        if (playerData.abilityTimer > 0)
            return;
        event.setCancelled(true);
        playerData.abilityTimer = 60;
        player.setVelocity(player.getVelocity().setY(1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 1, true, false));
    }
}
